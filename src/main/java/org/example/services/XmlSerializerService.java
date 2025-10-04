package org.example.services;

import org.example.annotations.XmlField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Field;

public class XmlSerializerService {

    public static void serialize(Object obj, String filename) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement(obj.getClass().getSimpleName());
        doc.appendChild(root);

        serializeObject(obj, root, doc);

        // Настройка красивого вывода
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
    }

    private static void serializeObject(Object obj, Element parent, Document doc) throws Exception {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(XmlField.class)) {
                Object value = field.get(obj);
                String tagName = field.getAnnotation(XmlField.class).name();
                if (tagName.isEmpty()) tagName = field.getName();

                if (value != null) {
                    if (value.getClass().isArray()) {
                        Element arrayElement = doc.createElement(tagName);
                        parent.appendChild(arrayElement);
                        Object[] array = (Object[]) value;
                        for (Object item : array) {
                            Element itemElement = doc.createElement("item");
                            serializeObject(item, itemElement, doc);
                            arrayElement.appendChild(itemElement);
                        }
                    } else {
                        Element element = doc.createElement(tagName);
                        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                            element.setTextContent(value.toString());
                        } else {
                            serializeObject(value, element, doc);
                        }
                        parent.appendChild(element);
                    }
                }
            }
        }
    }
}
