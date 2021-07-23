//package ua.projects.discordbot.controller;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.bind.annotation.RequestParam;
//import ua.projects.discordbot.persistence.*;
//import ua.projects.discordbot.repository.AttributeRepository;
//import ua.projects.discordbot.service.*;
//
//import java.util.List;
//
//import static org.mockito.AdditionalAnswers.returnsFirstArg;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class ApplicationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ApplicationController applicationController;
//
//    private AttributeService attributeService;
//
//    private CategoryService categoryService;
//
//    private FactionService factionService;
//
//    private RaceService raceService;
//
//    private UnitService unitService;
//
//    @BeforeEach
//    void init() {
//        attributeService = mock(AttributeService.class);
//        applicationController.setAttributeService(attributeService);
//        categoryService = mock(CategoryService.class);
//        applicationController.setCategoryService(categoryService);
//        factionService = mock(FactionService.class);
//        applicationController.setFactionService(factionService);
//        raceService = mock(RaceService.class);
//        applicationController.setRaceService(raceService);
//        unitService = mock(UnitService.class);
//        applicationController.setUnitService(unitService);
//    }
//
//    @Test
//    @Order(1)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createAttribute() throws Exception {
//        String attributeDescription = "myAttribute";
//        Attribute attribute = new Attribute(attributeDescription);
//        attribute.setId(1);
//
//        when(attributeService.create(attributeDescription)).thenReturn(attribute);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createAttribute")
//                .param("attributeName", attributeDescription))
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(2),
//                        model().attribute("id", attribute.getId()),
//                        model().attribute("description", attribute.getDescription())));
//        verify(attributeService, only()).create(attributeDescription);
//    }
//
//    @Test
//    @Order(2)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createCategory() throws Exception {
//        String unitCategory = "myCategory";
//        Category category = new Category(unitCategory);
//        category.setId(1);
//
//        when(categoryService.create(unitCategory)).thenReturn(category);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createCategory")
//                .param("unitCategory", unitCategory))
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(2),
//                        model().attribute("id", category.getId()),
//                        model().attribute("unitCategory", category.getUnitCategory())));
//        verify(categoryService, only()).create(unitCategory);
//    }
//
//    @Test
//    @Order(3)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createFaction() throws Exception {
//        String raceName = "race";
//        String factionName = "myFaction";
//        Race race = new Race(raceName);
//        Faction faction = new Faction(factionName);
//        faction.setRace(race);
//        faction.setId(1);
//
//        when(factionService.create(factionName, raceName)).thenReturn(faction);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createFaction")
//                .param("factionName", factionName)
//                .param("raceName", raceName))
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(3),
//                        model().attribute("id", faction.getId()),
//                        model().attribute("name", faction.getName()),
//                        model().attribute("race", race)));
//        verify(factionService, only()).create(factionName, raceName);
//    }
//
//    @Test
//    @Order(4)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createRace() throws Exception {
//        String raceName = "myRace";
//        Race race = new Race(raceName);
//        race.setId(1);
//
//        when(raceService.create(raceName)).thenReturn(race);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createRace")
//                .param("raceName", raceName))
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(2),
//                        model().attribute("id", race.getId()),
//                        model().attribute("name", race.getName())));
//        verify(raceService, only()).create(raceName);
//    }
//
//    @Test
//    @Order(4)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createRace() throws Exception {
//        String raceName = "myRace";
//        Race race = new Race(raceName);
//        race.setId(1);
//
//        when(raceService.create(raceName)).thenReturn(race);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createRace")
//                .param("raceName", raceName))
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(2),
//                        model().attribute("id", race.getId()),
//                        model().attribute("name", race.getName())));
//        verify(raceService, only()).create(raceName);
//    }
//
//
//
//    @Test
//    @Order(6)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    void createUnit() throws Exception {
//        String unitName = "myUnit";
//        String factionName = "myFaction";
//        String unitCategory = "myCategory";
//        Unit unit = new Unit(unitName);
//        unit.setId(1);
//
//        when(raceService.create(unitName)).thenReturn(unit);
//
//        mockMvc.perform(post("/total-war-warhammer/admin/createRace")
//                .param("raceName", raceName)
//        .param("name", unitName)
//        .param("factionName", factionName)
//        .param("unitCategory", unitCategory)
//        .param("weaponType") String weaponType,
//        .param("attributes") String attributes,
//        .param("cost", required = false, defaultValue = "0") Integer cost,
//        .param("upkeep", required = false, defaultValue = "0") Integer upkeep,
//        .param("health", required = false, defaultValue = "0") Integer health,
//        .param("leadership", required = false, defaultValue = "0") Integer leadership,
//        .param("speed", required = false, defaultValue = "0") Integer speed,
//        .param("meleeAttack", required = false, defaultValue = "0") Integer meleeAttack,
//        .param("meleeDefence", required = false, defaultValue = "0") Integer meleeDefence,
//        .param("chargeBonus", required = false, defaultValue = "0") Integer chargeBonus,
//        .param("missileResistance", required = false, defaultValue = "0") Integer missileResistance,
//        .param("magicResistance", required = false, defaultValue = "0") Integer magicResistance,
//        .param("armorProtection", required = false, defaultValue = "0") Integer armorProtection,
//        .param("weaponDamage", required = false, defaultValue = "0") Integer weaponDamage,
//        .param("armourPiercingDamage", required = false, defaultValue = "0") Integer armourPiercingDamage,
//        .param("meleeInterval", required = false, defaultValue = "0") Integer meleeInterval,
//        .param("magicalAttack", required = false, defaultValue = "0") Integer magicalAttack,
//        .param("range", required = false, defaultValue = "0") Integer range,
//        .param("unitSize", required = false, defaultValue = "0") Integer unitSize,
//        .param("turns", required = false, defaultValue = "0") Integer turns)
//
//                .andExpect(matchAll(
//                        status().isCreated(),
//                        model().size(2),
//                        model().attribute("id", race.getId()),
//                        model().attribute("name", race.getName())));
//        verify(raceService, only()).create(raceName);
//    }
//}