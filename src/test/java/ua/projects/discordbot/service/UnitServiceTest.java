package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitServiceTest {

    @Autowired
    private UnitService unitService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private FactionService factionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private AttributeService attributeService;

    @BeforeEach
    void testContextLoads() {
        assertNotNull(unitService);
        assertNotNull(raceService);
        assertNotNull(factionService);
        assertNotNull(categoryService);
    }

    @Test
    @Order(1)
    void createUnit() {
        Race race = raceService.create("Skaven");
        Faction faction = factionService.create("Clan Mors", race.getName());
        Category category = categoryService.create("Legendary Lord");
        Weapon weaponType = weaponService.create("Melee");
        Attribute encourage = attributeService.create("Encourage");
        Attribute hide = attributeService.create("Hide");
        String attributes = "Encourage, Hide";
        Set<Attribute> attributeSet = new HashSet<>(Arrays.asList(
                encourage,
                hide
        ));
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 63);
        parameters.put("setMeleeDefence", 42);
        String unitName = "Queek Headtaker";
        Unit testUnit = new Unit(unitName);
        testUnit.setFaction(faction);
        testUnit.setCategory(category);
        testUnit.setWeaponType(weaponType);
        testUnit.setAttributeSet(attributeSet);
        testUnit.setMeleeAttack(63);
        testUnit.setMeleeDefence(42);
        Unit modelUnit = unitService.create(unitName, faction.getName(), category.getUnitCategory(), weaponType.getType(), attributes, parameters);
        Assertions.assertEquals(testUnit.getFaction().getName(), modelUnit.getFaction().getName());
        Assertions.assertEquals(testUnit.getName(), modelUnit.getName());
        Assertions.assertEquals(testUnit.getCategory().getUnitCategory(), modelUnit.getCategory().getUnitCategory());
        Assertions.assertEquals(testUnit.getWeaponType().getType(), modelUnit.getWeaponType().getType());
        Assertions.assertEquals(testUnit.getAttributeSet().size(), modelUnit.getAttributeSet().size());
        Assertions.assertEquals(testUnit.getMeleeAttack(), modelUnit.getMeleeAttack());
        Assertions.assertEquals(testUnit.getMeleeDefence(), modelUnit.getMeleeDefence());
    }

    @Test
    @Order(2)
    void createUnitWithException() {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 63);
        parameters.put("setMeleeDefence", 42);
        Exception nullUnitName = assertThrows(ValidationException.class,
                () -> unitService.create(
                        null, "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", parameters));
        String expectedMessage = "Name is mandatory.";
        String actualMessage = nullUnitName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String name = "Queek Headtaker";
        Exception duplicateUnitName = assertThrows(ValidationException.class,
                () -> unitService.create(
                        name, "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", parameters));
        expectedMessage = "Unit with " + name + " presents in dataBase. Unit name should be unique.";
        actualMessage = duplicateUnitName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String validName = "Lord Skrolk";
        Exception nullFactionName = assertThrows(ValidationException.class,
                () -> unitService.create(
                        validName, null, "Legendary Lord", "Melee", "Encourage, Hide", parameters));
        expectedMessage = "Faction name is mandatory.";
        actualMessage = nullFactionName.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullCategory = assertThrows(ValidationException.class,
                () -> unitService.create(
                        validName, "Clan Mors", null, "Melee", "Encourage, Hide", parameters));
        expectedMessage = "Unit category is mandatory.";
        actualMessage = nullCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullWeapon = assertThrows(ValidationException.class,
                () -> unitService.create(
                        validName, "Clan Mors", "Legendary Lord", null, "Encourage, Hide", parameters));
        expectedMessage = "Weapon type is mandatory.";
        actualMessage = nullWeapon.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullAttribute = assertThrows(ValidationException.class,
                () -> unitService.create(
                        validName, "Clan Mors", "Legendary Lord", "Melee", null, parameters));
        expectedMessage = "Attributes name is mandatory.";
        actualMessage = nullAttribute.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Map<String, Integer> wrongParameters = new HashMap<>();
        wrongParameters.put("wrong", 63);
        Exception wrong = assertThrows(RuntimeException.class,
                () -> unitService.create(
                        validName, "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", wrongParameters));
        expectedMessage = "Error while invocation parameter set";
        actualMessage = wrong.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAll() {
        List<Unit> units = unitService.findAll();
        Assertions.assertTrue(units.size() > 0);
    }

    @Test
    @Order(4)
    void findById() {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 0);
        Unit unit = unitService.create("test unit", "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", parameters);
        Unit toCHeck = unitService.find(unit.getId());
        Assertions.assertEquals(toCHeck.getName(), unit.getName());
    }

    @Test
    @Order(5)
    void findByIdWithException() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> unitService.find(0));
        String expectedMessage = "Unit with id " + 0 + " not found";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> unitService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(6)
    void update() {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 63);
        Unit before = unitService.create(
                "before", "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", parameters);
        Unit toCHeck = unitService.update(
                before.getId(), "after", null, null, null, null, parameters);
        Assertions.assertEquals("after", toCHeck.getName());

        Faction faction = factionService.create("Clan Not Mors", "Skaven");
        toCHeck = unitService.update(before.getId(), null, "Clan Not Mors", null, null, null, null);
        Assertions.assertEquals(toCHeck.getFaction().getName(), faction.getName());

        Category category = categoryService.create("Hero");
        toCHeck = unitService.update(before.getId(), null, null, category.getUnitCategory(), null, null, null);
        Assertions.assertEquals(toCHeck.getCategory().getUnitCategory(), category.getUnitCategory());

        Weapon weaponType = weaponService.create("Range");
        toCHeck = unitService.update(before.getId(), null, null, null, weaponType.getType(), null, null);
        Assertions.assertEquals(toCHeck.getWeaponType().getType(), weaponType.getType());

        attributeService.create("Lazy");
        String attributes = "Lazy";
        toCHeck = unitService.update(before.getId(), null, null, null, null, attributes, null);
        Assertions.assertEquals(toCHeck.getAttributeSet().size(), 1);

        Map<String, Integer> wrongParameters = new HashMap<>();
        wrongParameters.put("wrong", 63);
        Exception exception = assertThrows(RuntimeException.class,
                () -> unitService.update(before.getId(), null, null, null, null, null, wrongParameters));
        String expectedMessage = "Error while invocation parameter set";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void delete() {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 63);
        parameters.put("setMeleeDefence", 42);
        Unit unit = unitService.create("forDelete", "Clan Mors", "Legendary Lord", "Melee", "Encourage, Hide", parameters);
        int before = unitService.findAll().size();
        unitService.delete(unit.getId());
        int after = unitService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void findUnitsForQuery() {
        Race race = raceService.create("Vampire");
        Faction sylvania = factionService.create("Sylvania", race.getName());
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setMeleeAttack", 63);
        parameters.put("setMeleeDefence", 42);
        unitService.create("Vlad von Carstein", sylvania.getName(), "Legendary Lord", "Melee", "Hide", parameters);
        unitService.create("Mannfred von Carstein", sylvania.getName(), "Legendary Lord", "Melee", "Hide", parameters);
        int size = unitService.getUnitsByFactionAndCategory(sylvania.getName(), "Legendary Lord").size();
        Assertions.assertEquals(2, size);
    }
}
