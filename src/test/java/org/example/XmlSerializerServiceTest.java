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

class XmlSerializerServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testSerializationAndDeserializationOnThreeClasses() throws Exception {

        Person person1 = new Person("Анна", 28);
        Person person2 = new Person("Иван", 32);

        Team team = new Team("Разработка", new Person[]{person1, person2});

        Company originalCompany = new Company("ТехноЛаб", team);

        File xmlFile = tempDir.resolve("company.xml").toFile();
        XmlSerializerService.serialize(originalCompany, xmlFile.getAbsolutePath());

        Company restoredCompany = XmlSerializerService.deserialize(Company.class, xmlFile.getAbsolutePath());

        assertEquals("ТехноЛаб", restoredCompany.getCompanyName());

        assertEquals("Разработка", restoredCompany.getMainTeam().getTeamName());

        assertEquals(2, restoredCompany.getMainTeam().getMembers().length);

        Person restoredPerson1 = restoredCompany.getMainTeam().getMembers()[0];
        assertEquals("Анна", restoredPerson1.getName());
        assertEquals(28, restoredPerson1.getAge());

        Person restoredPerson2 = restoredCompany.getMainTeam().getMembers()[1];
        assertEquals("Иван", restoredPerson2.getName());
        assertEquals(32, restoredPerson2.getAge());
    }
}
