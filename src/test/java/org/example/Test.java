package org.example;

import org.example.models.Company;
import org.example.models.Person;
import org.example.models.Team;
import org.example.services.XmlSerializerService;

import java.io.File;

public class Test {
    static int successTest = 0;

    public static void main(String[] args) {
        try {
            System.out.println("Запуск тестов\n");

            testRegular();
            testEmptyTeam();
            testSinglePersonTeam();

            System.out.println("\nТестов завершено успешно: " + successTest);

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testRegular() throws Exception {
        System.out.println("Test 1: Serialize");

        Person p1 = new Person("София", 30);
        Person p2 = new Person("Павел", 25);
        Team team = new Team("Разработчики", new Person[]{p1, p2});
        Company original = new Company("SegFault inc.", team);

        System.out.println("Оригинал создан");

        // Сериализация
        XmlSerializerService.serialize(original, "test_company.xml");
        System.out.println("Сериализовано в test_company.xml");

        // Десериализация
        Company restored = XmlSerializerService.deserialize(Company.class, "test_company.xml");
        System.out.println("Восстановлено из XML");

        boolean areEqual =
                original.getCompanyName().equals(restored.getCompanyName()) &&
                        original.getMainTeam().getTeamName().equals(restored.getMainTeam().getTeamName()) &&
                        original.getMainTeam().getMembers().length == restored.getMainTeam().getMembers().length &&
                        original.getMainTeam().getMembers()[0].getName().equals(restored.getMainTeam().getMembers()[0].getName());

        System.out.println("Объекты равны? " + areEqual);

        if (areEqual) {
            System.out.println("Test 1 PASSED");
            successTest++;
        } else {
            System.out.println("Test 1 FAILED");
        }
    }

    private static void testEmptyTeam() throws Exception {
        System.out.println("\nTest 2: Empty Team");

        // Тест с пустой командой
        Team emptyTeam = new Team("Empty Team", new Person[0]);
        Company company = new Company("Startup", emptyTeam);

        // Сериализация
        XmlSerializerService.serialize(company, "test_empty_team.xml");
        System.out.println("Сериализовано в test_empty_team.xml");

        // Десериализация
        Company restored = XmlSerializerService.deserialize(Company.class, "test_empty_team.xml");
        System.out.println("Восстановлено из XML");

        boolean areEqual =
                company.getCompanyName().equals(restored.getCompanyName()) &&
                        company.getMainTeam().getTeamName().equals(restored.getMainTeam().getTeamName()) &&
                        company.getMainTeam().getMembers().length == restored.getMainTeam().getMembers().length;

        System.out.println("Компания с пустой командой равны? " + areEqual);

        if (areEqual && restored.getMainTeam().getMembers().length == 0) {
            System.out.println("Test 2 PASSED");
            successTest++;
        } else {
            System.out.println("Test 2 FAILED");
        }
    }

    private static void testSinglePersonTeam() throws Exception {
        System.out.println("\nTest 3: Single Person Team");

        Person solo = new Person("Solo Developer", 30);
        Team soloTeam = new Team("Solo Team", new Person[]{solo});
        Company soloCompany = new Company("One Person Co", soloTeam);

        // Сериализация
        XmlSerializerService.serialize(soloCompany, "test_solo.xml");
        System.out.println("Сериализовано в test_empty_team.xml");

        // Десереализация
        Company restored = XmlSerializerService.deserialize(Company.class, "test_solo.xml");
        System.out.println("Восстановлено из XML");

        boolean areEqual =
                soloCompany.getCompanyName().equals(restored.getCompanyName()) &&
                        soloCompany.getMainTeam().getTeamName().equals(restored.getMainTeam().getTeamName()) &&
                        soloCompany.getMainTeam().getMembers().length == restored.getMainTeam().getMembers().length &&
                        soloCompany.getMainTeam().getMembers()[0].getName().equals(restored.getMainTeam().getMembers()[0].getName()) &&
                        soloCompany.getMainTeam().getMembers()[0].getAge() == restored.getMainTeam().getMembers()[0].getAge();

        System.out.println("Компания с одним разработчиком равны? " + areEqual);

        if (areEqual && restored.getMainTeam().getMembers().length == 1) {
            System.out.println("Test 3 PASSED");
            successTest++;
        } else {
            System.out.println("Test 3 FAILED");
        }
        cleanup();
    }

    private static void cleanup() {
        String[] testFiles = {
                "test_company.xml",
                "test_empty_team.xml",
                "test_solo.xml"
        };

        for (String filename : testFiles) {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}