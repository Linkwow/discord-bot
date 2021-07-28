package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Faction;
import ua.projects.discordbot.persistence.Race;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

//todo : change all service test from integration to unit. all test should be independent from each other
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FactionServiceTests {

    @Autowired
    private FactionService factionService;

    //todo : change for something needed
    @Autowired
    private RaceService raceService;

    @BeforeEach
    void testContextLoads() {
        assertNotNull(factionService);
        assertNotNull(raceService);
    }

    @Test
    @Order(1)
    public void createFaction() {
        String factionName = "new Faction";
        Faction testEntity = new Faction(factionName);
        Race race = raceService.create("new Race");
        testEntity.setRace(race);
        Faction modelEntity = factionService.create(factionName, "new Race");
        Assertions.assertEquals(modelEntity.getName(), testEntity.getName());
        Assertions.assertEquals(modelEntity.getRace().getName(), testEntity.getRace().getName());
    }

    @Test
    @Order(2)
    void exceptionWhenCreate() {
        Exception emptyName = assertThrows(ValidationException.class,
                () -> factionService.create(null, "new Race"));
        String expectedMessage = "Name is mandatory. Name should be a string";
        String actualMessage = emptyName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String name = "new Faction";
        Exception duplicateFaction = assertThrows(ValidationException.class,
                () -> factionService.create(name, "new Race"));
        expectedMessage = "Faction " + name + " presents in dataBase. Faction should be unique.";
        actualMessage = duplicateFaction.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String secondFaction = "new Faction2";
        Exception nullRace = assertThrows(ValidationException.class,
                () -> factionService.create(secondFaction, null));
        expectedMessage = "Race name is mandatory. Name should be a string";
        actualMessage = nullRace.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAllCategories() {
        factionService.create("first", "new Race");
        List<Faction> factions = factionService.findAll();
        Assertions.assertTrue(factions.size() > 0);
    }

    @Test
    @Order(4)
    void findFactionById() {
        Faction factionForId = factionService.create("second", "new Race");
        Faction faction = factionService.find(factionForId.getId());
        Assertions.assertEquals("second", faction.getName());
    }

    @Test
    @Order(5)
    void exceptionWhenFind() {
        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> factionService.find(0));
        String expectedMessage = "Faction with id " + id + " not found";
        String actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> factionService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void updateFactionById() {
        Faction beforeUpdate = factionService.create("beforeUpdate", "new Race");
        Faction afterUpdate = factionService.update(beforeUpdate.getId(), "afterUpdate", "new Race");
        Assertions.assertEquals("afterUpdate", afterUpdate.getName());
        Race race = raceService.create("new Race2");
        afterUpdate = factionService.update(beforeUpdate.getId(), null, "new Race2");
        Assertions.assertEquals(afterUpdate.getRace().getName(), race.getName());
        afterUpdate = factionService.update(beforeUpdate.getId(), null, null);
        Assertions.assertEquals(afterUpdate.getRace().getName(), race.getName());
        Assertions.assertEquals(afterUpdate.getName(), afterUpdate.getName());

    }

    @Test
    @Order(6)
    void exceptionWhenUpdate() {
        Exception nullId = assertThrows(ValidationException.class,
                () -> factionService.update(null, "new Faction", "new Race"));
        String expectedMessage = "Id is mandatory. Id should be a number.";
        String actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> factionService.find(0));
        expectedMessage = "Faction with id " + id + " not found";
        actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Faction simpleFaction = factionService.create("new Duplicate Faction", "new Race");
        String factionName = "new Duplicate Faction";
        Exception duplicateUnitCategory = assertThrows(ValidationException.class,
                () -> factionService.update(simpleFaction.getId(), factionName, null));
        expectedMessage = "Faction " + factionName + " presents in dataBase. Faction should be unique.";
        actualMessage = duplicateUnitCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void deleteCategoryById() {
        Faction categoryForDelete = factionService.create("delete", "new Race2");
        int before = factionService.findAll().size();
        factionService.delete(categoryForDelete.getId());
        int after = factionService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void getFactionByName() {
        String factionName = "new Faction";
        Faction read = factionService.getFactionByName(factionName);
        Assertions.assertEquals(factionName, read.getName());
    }

    @Test
    @Order(9)
    void getFactionsByRace(){
        raceService.create("The Empire");
        factionService.create("Reikland", "The Empire");
        factionService.create("The Golden Order", "The Empire");
        int size = factionService.getFactionsByRace("The Empire").size();
        Assertions.assertEquals(2, size);
    }

    @Test
    @Order(10)
    void getFactionByNameWithException(){
        String name = "empty faction";
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> factionService.getFactionByName(name));
        String expectedMessage = "Faction with name " + name + " does absence in data base";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
