package ua.projects.discordbot.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.projects.discordbot.persistence.Attribute;
import ua.projects.discordbot.persistence.Category;
import ua.projects.discordbot.persistence.Faction;
import ua.projects.discordbot.persistence.Race;
import ua.projects.discordbot.repository.AttributeRepository;
import ua.projects.discordbot.service.AttributeService;
import ua.projects.discordbot.service.CategoryService;
import ua.projects.discordbot.service.FactionService;

import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationController applicationController;

    private AttributeService attributeService;

    private CategoryService categoryService;

    private FactionService factionService;

    @BeforeEach
    void init() {
        attributeService = mock(AttributeService.class);
        applicationController.setAttributeService(attributeService);
        categoryService = mock(CategoryService.class);
        applicationController.setCategoryService(categoryService);
        factionService = mock(FactionService.class);
        applicationController.setFactionService(factionService);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createAttribute() throws Exception {
        when(attributeService.create(notNull())).thenAnswer(invocation -> {
            Attribute entity = new Attribute(invocation.getArgument(0));
            entity.setId(1);
            return entity;
        });
        mockMvc.perform(post("/total-war-warhammer/admin/createAttribute").param("attributeName", "attribute"))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attributeExists("id"),
                        model().attributeExists("description"),
                        model().attribute("id", 1),
                        model().attribute("description", "attribute")));
        verify(attributeService, only()).create("attribute");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createCategory() throws Exception {
        when(categoryService.create(notNull())).thenAnswer(invocation -> {
            Category entity = new Category(invocation.getArgument(0));
            entity.setId(1);
            return entity;
        });
        mockMvc.perform(post("/total-war-warhammer/admin/createCategory").param("unitCategory", "category"))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(2),
                        model().attributeExists("id"),
                        model().attributeExists("unitCategory"),
                        model().attribute("id", 1),
                        model().attribute("unitCategory", "category")));
        verify(categoryService, only()).create("category");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void createFaction() throws Exception {
        Race race = mock(Race.class);
        when(factionService.create(notNull(), notNull())).thenAnswer(invocation -> {
            Faction entity = new Faction(invocation.getArgument(0));
            race.setName("race");
            race.setId(1);
            entity.setRace(race);
            entity.setId(1);
            return entity;
        });
        mockMvc.perform(post("/total-war-warhammer/admin/createFaction")
                .param("factionName", "faction")
                .param("raceName", "race"))
                .andExpect(matchAll(
                        status().isCreated(),
                        model().size(4),
                        model().attributeExists("id"),
                        model().attributeExists("factionName"),
                        model().attributeExists("race"),
                        model().attribute("id", 1),
                        model().attribute("name", "faction"),
                        model().attribute("race", race),
                        model().attribute("name", race.getName())));
        verify(factionService, only()).create("faction", "race");
    }
}