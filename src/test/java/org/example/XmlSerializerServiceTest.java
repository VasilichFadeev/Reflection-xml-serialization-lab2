package org.example;

import org.example.models.Company;
import org.example.models.Person;
import org.example.models.Team;
import org.example.services.XmlSerializerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для XmlSerializerService
 * 1. Набор аннотированных полей
 * 2. Класс с массивом экземпляров первого класса
 * 3. Сложный объект для рекурсивной сериализации
 */
class XmlSerializerServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testSerializationAndDeserializationOnThreeClasses() throws Exception {
        // === Требование 1: Набор аннотированных полей ===
        // Класс Person содержит поля, помеченные @XmlField:
        //   @XmlField(name = "name") private String name;
        //   @XmlField(name = "age") private int age;
        Person person1 = new Person("Анна", 28);
        Person person2 = new Person("Иван", 32);

        // === Требование 2: Класс с массивом экземпляров первого класса ===
        // Класс Team содержит: @XmlField(name = "members") private Person[] members;
        // Это массив ССЫЛОК на объекты Person — именно то, что требуется
        Team team = new Team("Разработка", new Person[]{person1, person2});

        // === Требование 3: Сложный объект для рекурсивной сериализации ===
        // Класс Company содержит: @XmlField(name = "mainTeam") private Team mainTeam;
        // При сериализации происходит рекурсия: Company → Team → Person[] → Person
        Company originalCompany = new Company("ТехноЛаб", team);

        // Сохраняем в XML
        File xmlFile = tempDir.resolve("company.xml").toFile();
        XmlSerializerService.serialize(originalCompany, xmlFile.getAbsolutePath());

        // Загружаем обратно
        Company restoredCompany = XmlSerializerService.deserialize(Company.class, xmlFile.getAbsolutePath());

        // === Проверка: всё ли сохранилось корректно? ===
        // 1. Название компании
        assertEquals("ТехноЛаб", restoredCompany.getCompanyName());

        // 2. Название команды
        assertEquals("Разработка", restoredCompany.getMainTeam().getTeamName());

        // 3. Количество участников (массив не потерялся)
        assertEquals(2, restoredCompany.getMainTeam().getMembers().length);

        // 4. Данные первого участника (аннотированные поля)
        Person restoredPerson1 = restoredCompany.getMainTeam().getMembers()[0];
        assertEquals("Анна", restoredPerson1.getName());
        assertEquals(28, restoredPerson1.getAge());

        // 5. Данные второго участника
        Person restoredPerson2 = restoredCompany.getMainTeam().getMembers()[1];
        assertEquals("Иван", restoredPerson2.getName());
        assertEquals(32, restoredPerson2.getAge());
    }
}
