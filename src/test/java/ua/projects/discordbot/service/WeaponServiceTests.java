package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Weapon;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class WeaponServiceTests {

    @Autowired
    private WeaponService weaponService;

    @BeforeEach
    void testContextLoads() {
        assertNotNull(weaponService);
    }

    @Test
    @Order(1)
    public void createWeapon() {
        String weaponType = "Sword";
        Weapon testEntity = new Weapon(weaponType);
        Weapon modelEntity = weaponService.create(weaponType);
        Assertions.assertEquals(modelEntity.getType(), testEntity.getType());
    }

    @Test
    @Order(2)
    void exceptionWhenCreate() {
        Exception emptyWeaponType = assertThrows(ValidationException.class,
                () -> weaponService.create(null));
        String expectedMessage = "Weapon type is mandatory. Weapon type should be a string";
        String actualMessage = emptyWeaponType.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String duplicateType = "Sword";
        Exception duplicateWeaponType = assertThrows(ValidationException.class,
                () -> weaponService.create(duplicateType));
        expectedMessage = "Weapon " + duplicateType + " presents in dataBase. Weapon should be unique.";
        actualMessage = duplicateWeaponType.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAllWeapons() {
        weaponService.create("first");
        List<Weapon> weapons = weaponService.findAll();
        Assertions.assertTrue(weapons.size() > 0);
    }

    @Test
    @Order(4)
    void findWeaponById() {
        Weapon weaponForId = weaponService.create("second");
        Weapon weapon = weaponService.find(weaponForId.getId());
        Assertions.assertEquals("second", weapon.getType());
    }

    @Test
    @Order(5)
    void exceptionWhenFind() {
        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> weaponService.find(0));
        String expectedMessage = "Weapon type with id " + id + " not found";
        String actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> weaponService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void updateWeaponById() {
        Weapon beforeUpdate = weaponService.create("beforeUpdate");
        Weapon afterUpdate = weaponService.update(beforeUpdate.getId(), "afterUpdate");
        Assertions.assertEquals("afterUpdate", afterUpdate.getType());
    }

    @Test
    @Order(6)
    void exceptionWhenUpdate() {
        Exception nullId = assertThrows(ValidationException.class,
                () -> weaponService.update(null, "without id but with race name"));
        String expectedMessage = "Id is mandatory. Id should be a number.";
        String actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Weapon weaponForUpdate = weaponService.create("update");
        Exception nullWeaponType = assertThrows(ValidationException.class,
                () -> weaponService.update(weaponForUpdate.getId(), null));
        expectedMessage = "Weapon type is mandatory. Weapon type should be a string";
        actualMessage = nullWeaponType.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> weaponService.find(0));
        expectedMessage = "Weapon type with id " + id + " not found";
        actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Weapon simpleWeapon = weaponService.create("duplicate again");
        String weaponType = "duplicate again";
        Exception duplicateWeaponType = assertThrows(ValidationException.class,
                () -> weaponService.update(simpleWeapon.getId(), weaponType));
        expectedMessage = "Weapon " + weaponType + " presents in dataBase. Weapon should be unique.";
        actualMessage = duplicateWeaponType.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void deleteWeaponById() {
        Weapon weaponForDelete = weaponService.create("delete");
        int before = weaponService.findAll().size();
        weaponService.delete(weaponForDelete.getId());
        int after = weaponService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void getWeaponByName() {
        String weaponType = "Bow";
        weaponService.create(weaponType);
        Weapon read = weaponService.getWeaponByType(weaponType);
        Assertions.assertEquals(weaponType, read.getType());
    }

    @Test
    @Order(9)
    void getRaceByNameWithException(){
        String type = "no such weapon";
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> weaponService.getWeaponByType(type));
        String expectedMessage = "Weapon with type " + type + " does absence in data base";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
