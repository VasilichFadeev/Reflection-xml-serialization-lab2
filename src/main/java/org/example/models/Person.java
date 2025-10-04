package org.example.models;

import org.example.annotations.XmlField;

public class Person {
    @XmlField(name = "name")
    private String name;

    @XmlField(name = "age")
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
