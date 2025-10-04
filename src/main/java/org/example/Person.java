package org.example;

public class Person {
    @XmlField
    private String name;

    @XmlField(name = "years")
    private int age;

    @XmlField
    private double height;

    public Person(String name, int age, double height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    public Person() {}

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", height=" + height + "}";
    }
}
