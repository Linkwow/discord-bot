package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Race;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

//todo : change all service test from integration to unit. all test should be independent from each other
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RaceServiceTests {

    @Autowired
    private RaceService raceService;

    //todo : change for something needed
    @BeforeEach
    void testContextLoads() {
        assertNotNull(raceService);
    }

    @Test
    @Order(1)
    public void createRace() {
        String raceName = "Orks";
        Race testEntity = new Race(raceName);
        Race modelEntity = raceService.create(raceName);
        Assertions.assertEquals(modelEntity.getName(), testEntity.getName());
    }

    @Test
    @Order(2)
    void exceptionWhenCreate() {
        Exception emptyRaceName = assertThrows(ValidationException.class,
                () -> raceService.create(null));
        String expectedMessage = "Race name is mandatory. Race name should be a string";
        String actualMessage = emptyRaceName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String duplicateName = "Orks";
        Exception duplicateRaceName = assertThrows(ValidationException.class,
                () -> raceService.create(duplicateName));
        expectedMessage = "Race " + duplicateName + " presents in dataBase. Race should be unique.";
        actualMessage = duplicateRaceName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAllRaces() {
        raceService.create("first");
        List<Race> races = raceService.findAll();
        Assertions.assertTrue(races.size() > 0);
    }

    @Test
    @Order(4)
    void findRaceById() {
        Race raceForId = raceService.create("second");
        Race race = raceService.find(raceForId.getId());
        Assertions.assertEquals("second", race.getName());
    }

    @Test
    @Order(5)
    void exceptionWhenFind() {
        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> raceService.find(0));
        String expectedMessage = "Race with id " + id + " not found";
        String actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> raceService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void updateRaceById() {
        Race beforeUpdate = raceService.create("beforeUpdate");
        Race afterUpdate = raceService.update(beforeUpdate.getId(), "afterUpdate");
        Assertions.assertEquals("afterUpdate", afterUpdate.getName());
    }

    @Test
    @Order(6)
    void exceptionWhenUpdate() {
        Exception nullId = assertThrows(ValidationException.class,
                () -> raceService.update(null, "without id but with race name"));
        String expectedMessage = "Id is mandatory. Id should be a number.";
        String actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Race raceForUpdate = raceService.create("update");
        Exception nullRaceName = assertThrows(ValidationException.class,
                () -> raceService.update(raceForUpdate.getId(), null));
        expectedMessage = "Race name is mandatory. Race name should be a string";
        actualMessage = nullRaceName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> raceService.find(0));
        expectedMessage = "Race with id " + id + " not found";
        actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Race simpleRace = raceService.create("duplicate again");
        String raceName = "duplicate again";
        Exception duplicateRaceName = assertThrows(ValidationException.class,
                () -> raceService.update(simpleRace.getId(), raceName));
        expectedMessage = "Race " + raceName + " presents in dataBase. Race should be unique.";
        actualMessage = duplicateRaceName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void deleteRaceById() {
        Race raceForDelete = raceService.create("delete");
        int before = raceService.findAll().size();
        raceService.delete(raceForDelete.getId());
        int after = raceService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void getRaceByName() {
        String raceName = "Elfs";
        raceService.create(raceName);
        Race read = raceService.getRaceByName(raceName);
        Assertions.assertEquals(raceName, read.getName());
    }

    @Test
    @Order(9)
    void getRaceByNameWithException(){
        String name = "no such race";
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> raceService.getRaceByName(name));
        String expectedMessage = "Race with name " + name + " does absence in data base";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
