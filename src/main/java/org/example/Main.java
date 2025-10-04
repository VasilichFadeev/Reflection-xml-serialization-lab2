// src/main/java/org/example/Main.java
package org.example;

import org.example.models.Company;
import org.example.models.Person;
import org.example.models.Team;
import org.example.services.XmlSerializerService;

public class Main {
    public static void main(String[] args) {
        try {
            // Создаём объекты
            Person p1 = new Person("Alice", 30);
            Person p2 = new Person("Bob", 25);
            Team team = new Team("Developers", new Person[]{p1, p2});
            Company original = new Company("TechCorp", team);

            System.out.println("Оригинал:");
            System.out.println(original);

            // Сериализация
            XmlSerializerService.serialize(original, "company.xml");
            System.out.println("\nСериализовано в company.xml");

            // Десериализация
            Company restored = XmlSerializerService.deserialize(Company.class, "company.xml");
            System.out.println("\nВосстановлено:");
            System.out.println(restored);

            // Проверка равенства (по содержимому)
            boolean areEqual =
                original.getCompanyName().equals(restored.getCompanyName()) &&
                original.getMainTeam().getTeamName().equals(restored.getMainTeam().getTeamName()) &&
                original.getMainTeam().getMembers().length == restored.getMainTeam().getMembers().length &&
                original.getMainTeam().getMembers()[0].getName().equals(restored.getMainTeam().getMembers()[0].getName());
            System.out.println("\nРавны? " + areEqual);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
