package org.example.controllers;

import org.example.models.*;
import org.example.services.XmlSerializerService;
import org.example.views.ConsoleView;

public class AppController {
    private final ConsoleView view = new ConsoleView();

    public void run() {
        boolean running = true;
        while (running) {
            int choice = view.showMenu();

            switch (choice) {
                case 1 -> handleInputAndSave();
                case 2 -> handleLoad();
                case 3 -> {
                    running = false;
                    view.showMessage("Выход из программы...");
                }
                default -> view.showMessage("Неверный выбор, попробуйте снова!");
            }
        }
    }

    private void handleInputAndSave() {
        String companyName = view.readString("Введите название компании: ");
        String teamName = view.readString("Введите название команды: ");

        int memberCount = view.readInt("Введите количество участников команды: ");
        Person[] members = new Person[memberCount];

        for (int i = 0; i < memberCount; i++) {
            String name = view.readString("Имя участника #" + (i + 1) + ": ");
            int age = view.readInt("Возраст участника #" + (i + 1) + ": ");
            members[i] = new Person(name, age);
        }

        Team team = new Team(teamName, members);
        Company company = new Company(companyName, team);

        try {
            XmlSerializerService.serialize(company, "company.xml");
            view.showMessage("Данные успешно сохранены в company.xml");
        } catch (Exception e) {
            view.showMessage("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void handleLoad() {
        try {
            Company company = XmlSerializerService.deserialize(Company.class, "company.xml");
            view.showMessage("Данные загружены из company.xml:");
            view.showMessage("Компания: " + company.getCompanyName());
            view.showMessage("Команда: " + company.getMainTeam().getTeamName());
            for (Person p : company.getMainTeam().getMembers()) {
                view.showMessage("  - " + p.getName() + ", возраст " + p.getAge());
            }
        } catch (Exception e) {
            view.showMessage("Ошибка при загрузке: " + e.getMessage());
        }
    }
}
