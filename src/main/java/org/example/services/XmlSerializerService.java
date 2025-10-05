package org.example.services;

import org.example.annotations.XmlField;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class XmlSerializerService {

    public static void serialize(Object obj, String filename) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement(obj.getClass().getSimpleName());
        doc.appendChild(root);
        serializeObject(obj, root, doc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new DOMSource(doc), new StreamResult(new File(filename)));
    }

    private static void serializeObject(Object obj, Element parent, Document doc) throws Exception {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XmlField.class)) {
                Object value = field.get(obj);
                String tagName = field.getAnnotation(XmlField.class).name();
                if (tagName.isEmpty()) tagName = field.getName();

                Element elem = doc.createElement(tagName);
                if (value == null) {
                    parent.appendChild(elem);
                    continue;
                }

                if (value.getClass().isArray()) {
                    Object[] arr = (Object[]) value;
                    for (Object item : arr) {
                        Element itemElem = doc.createElement("item");
                        if (item instanceof String || item instanceof Number || item instanceof Boolean) {
                            itemElem.setTextContent(item.toString());
                        } else {
                            serializeObject(item, itemElem, doc);
                        }
                        elem.appendChild(itemElem);
                    }
                } else if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                    elem.setTextContent(value.toString());
                } else {
                    serializeObject(value, elem, doc); // рекурсия
                }
                parent.appendChild(elem);
            }
        }
    }

    public static <T> T deserialize(Class<T> clazz, String filename) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        T instance = clazz.getDeclaredConstructor().newInstance();
        deserializeObject(instance, root);
        return instance;
    }

    private static void deserializeObject(Object obj, Element elem) throws Exception {
        Class<?> clazz = obj.getClass();
        NodeList children = elem.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element child = (Element) node;
            String fieldName = child.getNodeName();

            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }

            if (!field.isAnnotationPresent(XmlField.class)) continue;

            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType.isArray()) {
                // Обработка массива
                NodeList items = child.getElementsByTagName("item");
                Class<?> componentType = fieldType.getComponentType();
                Object array = Array.newInstance(componentType, items.getLength());

                for (int j = 0; j < items.getLength(); j++) {
                    Element itemElem = (Element) items.item(j);
                    Object itemObj = componentType.getDeclaredConstructor().newInstance();
                    deserializeObject(itemObj, itemElem);
                    Array.set(array, j, itemObj);
                }
                field.set(obj, array);
            } else if (fieldType == String.class) {
                field.set(obj, child.getTextContent());
            } else if (fieldType == int.class || fieldType == Integer.class) {
                field.set(obj, Integer.parseInt(child.getTextContent()));
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                field.set(obj, Boolean.parseBoolean(child.getTextContent()));
            } else {
                // Вложенный объект
                Object nested = fieldType.getDeclaredConstructor().newInstance();
                deserializeObject(nested, child);
                field.set(obj, nested);
            }
        }
    }
}
