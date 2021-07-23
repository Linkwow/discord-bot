package ua.projects.discordbot.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ua.projects.discordbot.persistence.*;
import ua.projects.discordbot.service.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationController applicationController;

    private AttributeService attributeService;

    private CategoryService categoryService;

    private FactionService factionService;

    private RaceService raceService;

    private UnitService unitService;

    private WeaponService weaponService;

    @BeforeEach
    void init() {
        attributeService = mock(AttributeService.class);
        applicationController.setAttributeService(attributeService);
        categoryService = mock(CategoryService.class);
        applicationController.setCategoryService(categoryService);
        factionService = mock(FactionService.class);
        applicationController.setFactionService(factionService);
        raceService = mock(RaceService.class);
        applicationController.setRaceService(raceService);
        unitService = mock(UnitService.class);
        applicationController.setUnitService(unitService);
        weaponService = mock(WeaponService.class);
        applicationController.setWeaponService(weaponService);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createAttribute() throws Exception {
        String attributeDescription = "myAttributeToCreate";
        Attribute attribute = new Attribute(attributeDescription);
        attribute.setId(1);

        when(attributeService.create(attributeDescription)).thenReturn(attribute);

        mockMvc.perform(post("/total-war-warhammer/admin/createAttribute")
                .param("attributeName", attributeDescription))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attribute("id", attribute.getId()),
                        model().attribute("description", attribute.getDescription())));
        verify(attributeService, only()).create(attributeDescription);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createCategory() throws Exception {
        String unitCategory = "myCategory";
        Category category = new Category(unitCategory);
        category.setId(1);

        when(categoryService.create(unitCategory)).thenReturn(category);

        mockMvc.perform(post("/total-war-warhammer/admin/createCategory")
                .param("unitCategory", unitCategory))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attribute("id", category.getId()),
                        model().attribute("unitCategory", category.getUnitCategory())));
        verify(categoryService, only()).create(unitCategory);
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createFaction() throws Exception {
        String raceName = "race";
        String factionName = "myFaction";
        Race race = new Race(raceName);
        Faction faction = new Faction(factionName);
        faction.setRace(race);
        faction.setId(1);

        when(factionService.create(factionName, raceName)).thenReturn(faction);

        mockMvc.perform(post("/total-war-warhammer/admin/createFaction")
                .param("factionName", factionName)
                .param("raceName", raceName))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(3),
                        model().attribute("id", faction.getId()),
                        model().attribute("name", faction.getName()),
                        model().attribute("race", race)));
        verify(factionService, only()).create(factionName, raceName);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createRace() throws Exception {
        String raceName = "myRace";
        Race race = new Race(raceName);
        race.setId(1);

        when(raceService.create(raceName)).thenReturn(race);

        mockMvc.perform(post("/total-war-warhammer/admin/createRace")
                .param("raceName", raceName))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attribute("id", race.getId()),
                        model().attribute("name", race.getName())));
        verify(raceService, only()).create(raceName);
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createWeapon() throws Exception {
        String weaponType = "myWeapon";
        Weapon weapon = new Weapon(weaponType);
        weapon.setId(1);

        when(weaponService.create(weaponType)).thenReturn(weapon);

        mockMvc.perform(post("/total-war-warhammer/admin/createWeapon")
                .param("type", weaponType))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attribute("id", weapon.getId()),
                        model().attribute("type", weapon.getType())));
        verify(weaponService, only()).create(weaponType);
    }


    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createUnit() throws Exception {
        String unitName = "myUnit";
        String factionName = "myFaction";
        String unitCategory = "myCategory";
        String weaponType = "myWeapon";
        String attributeDescription = "myAttribute";
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setCost", 0);
        parameters.put("setUpkeep", 0);
        parameters.put("setHealth", 0);
        parameters.put("setLeadership", 0);
        parameters.put("setSpeed", 0);
        parameters.put("setMeleeAttack", 0);
        parameters.put("setMeleeDefence", 0);
        parameters.put("setChargeBonus", 0);
        parameters.put("setMissileResistance", 0);
        parameters.put("setMagicResistance", 0);
        parameters.put("setArmorProtection", 0);
        parameters.put("setWeaponDamage", 0);
        parameters.put("setArmourPiercingDamage", 0);
        parameters.put("setMeleeInterval", 0);
        parameters.put("setMagicalAttack", 0);
        parameters.put("setRange", 0);
        parameters.put("setUnitSize", 0);
        parameters.put("setTurns", 0);
        Unit unit = new Unit(unitName);
        unit.setId(1);
        unit.setFaction(new Faction(factionName));
        setUnitParameters(unit, parameters);

        when(unitService.create(unitName,factionName, unitCategory, weaponType, attributeDescription, parameters)).thenReturn(unit);

        mockMvc.perform(post("/total-war-warhammer/admin/createUnit")
                .param("name", unitName)
                .param("factionName", factionName)
                .param("unitCategory", unitCategory)
                .param("weaponType", weaponType)
                .param("attributes", attributeDescription))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attribute("unit", unit),
                        model().attribute("attributes", unit.getAttributeSet())));
        verify(unitService, only()).create(unitName,factionName, unitCategory, weaponType, attributeDescription, parameters);
    }

    @Test
    @Order(7)
    void findAttributeById() throws Exception {
        String attributeDescription = "myAttributeToFind";
        Attribute attribute = new Attribute(attributeDescription);
        Integer id = 2;
        attribute.setId(id);

        when(attributeService.find(id)).thenReturn(attribute);

        mockMvc.perform(get("/total-war-warhammer/user/getAttributeById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("attributes", attribute)));
        verify(attributeService, only()).find(id);
    }

    @Test
    @Order(8)
    void findCategoryById() throws Exception {
        String unitCategory = "myCategoryToFind";
        Category category = new Category(unitCategory);
        Integer id = 2;
        category.setId(id);

        when(categoryService.find(id)).thenReturn(category);

        mockMvc.perform(get("/total-war-warhammer/user/getCategoryById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("categories", category)));
        verify(categoryService, only()).find(id);
    }

    @Test
    @Order(9)
    void findFactionById() throws Exception {
        String factionName = "myFactionToFind";
        Faction faction = new Faction(factionName);
        Integer id = 2;
        faction.setId(id);

        when(factionService.find(id)).thenReturn(faction);

        mockMvc.perform(get("/total-war-warhammer/user/getFactionById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("factions", faction)));
        verify(factionService, only()).find(id);
    }

    @Test
    @Order(10)
    void findRaceById() throws Exception {
        String raceName = "myRaceToFind";
        Race race = new Race(raceName);
        Integer id = 2;
        race.setId(id);

        when(raceService.find(id)).thenReturn(race);

        mockMvc.perform(get("/total-war-warhammer/user/getRaceById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("races", race)));
        verify(raceService, only()).find(id);
    }

    @Test
    @Order(11)
    void findUnitById() throws Exception {
        String unitName = "myUnitToFind";
        Unit unit = new Unit(unitName);
        Integer id = 2;
        unit.setId(id);
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setCost", 0);
        parameters.put("setUpkeep", 0);
        parameters.put("setHealth", 0);
        parameters.put("setLeadership", 0);
        parameters.put("setSpeed", 0);
        parameters.put("setMeleeAttack", 0);
        parameters.put("setMeleeDefence", 0);
        parameters.put("setChargeBonus", 0);
        parameters.put("setMissileResistance", 0);
        parameters.put("setMagicResistance", 0);
        parameters.put("setArmorProtection", 0);
        parameters.put("setWeaponDamage", 0);
        parameters.put("setArmourPiercingDamage", 0);
        parameters.put("setMeleeInterval", 0);
        parameters.put("setMagicalAttack", 0);
        parameters.put("setRange", 0);
        parameters.put("setUnitSize", 0);
        parameters.put("setTurns", 0);
        setUnitParameters(unit, parameters);

        when(unitService.find(id)).thenReturn(unit);

        mockMvc.perform(get("/total-war-warhammer/user/getUnitById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("units", unit)));
        verify(unitService, only()).find(id);
    }

    @Test
    @Order(12)
    void findWeaponById() throws Exception {
        String weaponType = "myWeaponToFind";
        Weapon weapon = new Weapon(weaponType);
        Integer id = 2;
        weapon.setId(id);

        when(weaponService.find(id)).thenReturn(weapon);

        mockMvc.perform(get("/total-war-warhammer/user/getWeaponById")
                .param("id", "2"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("weapons", weapon)));
        verify(weaponService, only()).find(id);
    }

    @Test
    @Order(13)
    void findAllAttributes() throws Exception {
        String attributeDescription = "attributeFromList";
        Attribute first = new Attribute(attributeDescription);
        first.setId(1);

        Attribute second = new Attribute(attributeDescription);
        second.setId(2);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(first);
        attributes.add(second);

        when(attributeService.findAll()).thenReturn(attributes);

        mockMvc.perform(get("/total-war-warhammer/user/getAttributes"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("attributes", attributes)));
        verify(attributeService, only()).findAll();
    }

    @Test
    @Order(14)
    void findAllCategories() throws Exception {
        String category = "categoryFromList";
        Category first = new Category(category);
        first.setId(1);

        Category second = new Category(category);
        second.setId(2);

        List<Category> categories = new ArrayList<>();
        categories.add(first);
        categories.add(second);

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/total-war-warhammer/user/getCategories"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("categories", categories)));
        verify(categoryService, only()).findAll();
    }

    @Test
    @Order(15)
    void findAllFactions() throws Exception {
        String faction = "factionFromList";
        Faction first = new Faction(faction);
        first.setId(1);

        Faction second = new Faction(faction);
        second.setId(2);

        List<Faction> factions = new ArrayList<>();
        factions.add(first);
        factions.add(second);

        when(factionService.findAll()).thenReturn(factions);

        mockMvc.perform(get("/total-war-warhammer/user/getFactions"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("factions", factions)));
        verify(factionService, only()).findAll();
    }

    @Test
    @Order(16)
    void findAllRaces() throws Exception {
        String race = "raceFromList";
        Race first = new Race(race);
        first.setId(1);

        Race second = new Race(race);
        second.setId(2);

        List<Race> races = new ArrayList<>();
        races.add(first);
        races.add(second);

        when(raceService.findAll()).thenReturn(races);

        mockMvc.perform(get("/total-war-warhammer/user/getRaces"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("races", races)));
        verify(raceService, only()).findAll();
    }

    @Test
    @Order(17)
    void findAllUnits() throws Exception {
        String unit = "unitFromList";
        Unit first = new Unit(unit);
        first.setId(1);

        Unit second = new Unit(unit);
        second.setId(2);
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setCost", 0);
        parameters.put("setUpkeep", 0);
        parameters.put("setHealth", 0);
        parameters.put("setLeadership", 0);
        parameters.put("setSpeed", 0);
        parameters.put("setMeleeAttack", 0);
        parameters.put("setMeleeDefence", 0);
        parameters.put("setChargeBonus", 0);
        parameters.put("setMissileResistance", 0);
        parameters.put("setMagicResistance", 0);
        parameters.put("setArmorProtection", 0);
        parameters.put("setWeaponDamage", 0);
        parameters.put("setArmourPiercingDamage", 0);
        parameters.put("setMeleeInterval", 0);
        parameters.put("setMagicalAttack", 0);
        parameters.put("setRange", 0);
        parameters.put("setUnitSize", 0);
        parameters.put("setTurns", 0);
        setUnitParameters(first, parameters);
        setUnitParameters(second, parameters);

        List<Unit> units = new ArrayList<>();
        units.add(first);
        units.add(second);

        when(unitService.findAll()).thenReturn(units);

        mockMvc.perform(get("/total-war-warhammer/user/getUnits"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("units", units)));
        verify(unitService, only()).findAll();
    }

    @Test
    @Order(18)
    void findAllWeapons() throws Exception {
        String weapon = "weaponFromList";
        Weapon first = new Weapon(weapon);
        first.setId(1);

        Weapon second = new Weapon(weapon);
        second.setId(2);

        List<Weapon> weapons = new ArrayList<>();
        weapons.add(first);
        weapons.add(second);

        when(weaponService.findAll()).thenReturn(weapons);

        mockMvc.perform(get("/total-war-warhammer/user/getWeapons"))
                .andExpect(matchAll(
                        status().isOk(),
                        model().size(1),
                        model().attribute("weapons", weapons)));
        verify(weaponService, only()).findAll();
    }

    @Test
    @Order(19)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateAttributesById() throws Exception {
        String attributeDescription = "updateAttribute";
        Attribute attribute = new Attribute(attributeDescription);
        Integer id = 3;
        attribute.setId(id);

        when(attributeService.update(id, attributeDescription)).thenReturn(attribute);

        mockMvc.perform(put("/total-war-warhammer/admin/updateAttribute")
                .param("id", "3")
                .param("description", attributeDescription))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(attributeService, only()).update(id, attributeDescription);
    }

    @Test
    @Order(20)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateCategoriesById() throws Exception {
        String unitCategory = "updateCategory";
        Category category = new Category(unitCategory);
        Integer id = 3;
        category.setId(id);

        when(categoryService.update(id, unitCategory)).thenReturn(category);

        mockMvc.perform(put("/total-war-warhammer/admin/updateCategory")
                .param("id", "3")
                .param("unitCategory", unitCategory))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(categoryService, only()).update(id, unitCategory);
    }

    @Test
    @Order(21)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateFactionsById() throws Exception {
        String name = "updateFaction";
        Race race = new Race("forUpdate");
        Faction faction = new Faction(name);
        Integer id = 3;
        faction.setId(id);

        when(factionService.update(id, name,race.getName())).thenReturn(faction);

        mockMvc.perform(put("/total-war-warhammer/admin/updateFaction")
                .param("id", "3")
                .param("factionName", name)
                .param("raceName", race.getName()))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(factionService, only()).update(id, name, race.getName());
    }

    @Test
    @Order(22)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateRaceById() throws Exception {
        String name = "updateRace";
        Race race = new Race(name);
        Integer id = 3;
        race.setId(id);

        when(raceService.update(id, name)).thenReturn(race);

        mockMvc.perform(put("/total-war-warhammer/admin/updateRace")
                .param("id", "3")
                .param("name", name))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(raceService, only()).update(id, name);
    }

    @Test
    @Order(23)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateUnitById() throws Exception {
        String unitName = "unitForUpdate";
        String factionName = "updateFaction";
        String unitCategory = "updateCategory";
        String weaponType = "updateWeapon";
        String attributeDescription = "updateAttribute";
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setCost", 0);
        parameters.put("setUpkeep", 0);
        parameters.put("setHealth", 0);
        parameters.put("setLeadership", 0);
        parameters.put("setSpeed", 0);
        parameters.put("setMeleeAttack", 0);
        parameters.put("setMeleeDefence", 0);
        parameters.put("setChargeBonus", 0);
        parameters.put("setMissileResistance", 0);
        parameters.put("setMagicResistance", 0);
        parameters.put("setArmorProtection", 0);
        parameters.put("setWeaponDamage", 0);
        parameters.put("setArmourPiercingDamage", 0);
        parameters.put("setMeleeInterval", 0);
        parameters.put("setMagicalAttack", 0);
        parameters.put("setRange", 0);
        parameters.put("setUnitSize", 0);
        parameters.put("setTurns", 0);
        Unit unit = new Unit(unitName);
        Integer id = 3;
        unit.setId(id);
        unit.setFaction(new Faction(factionName));
        setUnitParameters(unit, parameters);

        when(unitService.update(id, unitName, factionName, unitCategory, weaponType, attributeDescription, parameters)).thenReturn(unit);

        mockMvc.perform(put("/total-war-warhammer/admin/updateUnit")
                .param("id", "3")
                .param("name", unitName)
                .param("factionName", factionName)
                .param("unitCategory", unitCategory)
                .param("weaponType", weaponType)
                .param("attributes", attributeDescription))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(unitService, only()).update(id, unitName, factionName, unitCategory, weaponType, attributeDescription, parameters);
    }

    @Test
    @Order(24)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void updateWeaponById() throws Exception {
        String type = "updateWeapon";
        Weapon weapon = new Weapon(type);
        Integer id = 3;
        weapon.setId(id);

        when(weaponService.update(id, type)).thenReturn(weapon);

        mockMvc.perform(put("/total-war-warhammer/admin/updateWeapon")
                .param("id", "3")
                .param("name", type))
                .andExpect(matchAll(
                        status().isNoContent()));
        verify(weaponService, only()).update(id, type);
    }

    @Test
    @Order(25)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteAttributeById() throws Exception {
        Integer id = 3;

        doNothing().when(attributeService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteAttribute")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(attributeService, only()).delete(id);
    }

    @Test
    @Order(26)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteCategoryById() throws Exception {
        Integer id = 3;

        doNothing().when(categoryService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteCategory")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(categoryService, only()).delete(id);
    }

    @Test
    @Order(27)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteFactionById() throws Exception {
        Integer id = 3;

        doNothing().when(factionService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteFaction")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(factionService, only()).delete(id);
    }

    @Test
    @Order(28)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteUnitById() throws Exception {
        Integer id = 3;

        doNothing().when(unitService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteUnit")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(unitService, only()).delete(id);
    }

    @Test
    @Order(29)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteRaceById() throws Exception {
        Integer id = 3;

        doNothing().when(raceService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteRace")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(raceService, only()).delete(id);
    }

    @Test
    @Order(30)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void deleteWeaponById() throws Exception {
        Integer id = 3;

        doNothing().when(weaponService).delete(id);

        mockMvc.perform(delete("/total-war-warhammer/admin/deleteWeapon")
                .param("id", "3"))
                .andExpect(matchAll(
                        status().isOk()));
        verify(weaponService, only()).delete(id);
    }


    private void setUnitParameters(Unit unit, Map<String, Integer> parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (String methodName : parameters.keySet()) {
            if (parameters.get(methodName) != null) {
                Method method = unit.getClass().getMethod(methodName, Integer.class);
                method.invoke(unit, parameters.get(methodName));
            }
        }
    }

}