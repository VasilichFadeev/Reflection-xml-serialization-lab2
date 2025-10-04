package org.example;

import org.example.models.Company;
import org.example.models.Person;
import org.example.models.Team;
import org.example.services.XmlSerializerService;

public class Main {
    public static void main(String[] args) {
        try {
            Person p1 = new Person("Alice", 30);
            Person p2 = new Person("Bob", 25);

            Team team = new Team("Developers", new Person[]{p1, p2});
            Company company = new Company("TechCorp", team);

            XmlSerializerService.serialize(company, "company.xml");
            System.out.println("Сериализация завершена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
