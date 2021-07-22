package ua.projects.discordbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Attr;
import ua.projects.discordbot.bot.SlashCommandCreator;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Attribute;
import ua.projects.discordbot.repository.AttributeRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;


public class MockClass {

    private AttributeService attributeService;

    private AttributeRepository attributeRepository;

    private SlashCommandCreator slashCommandCreator;

    @BeforeEach
    void testContextLoads() {
        attributeRepository = mock(AttributeRepository.class);
        slashCommandCreator = mock(SlashCommandCreator.class);
        attributeService = new AttributeService(attributeRepository);
        attributeService.setSlashCommandCreator(slashCommandCreator);
    }

    @Test
    void simple() {


        Attribute attribute = new Attribute("title");
        attribute.setId(1);

        when(attributeRepository.save(notNull())).thenAnswer(invocation -> {
            Attribute entity = invocation.getArgument(1);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getDescription()).isEqualTo(attribute.getDescription());
            entity.setId(1);
            return entity;
        });

        Attribute response = attributeService.create(attribute.getDescription());

        assertThat(response.getId()).isEqualTo(attribute.getId());
        assertThat(response.getDescription()).isEqualTo(attribute.getDescription());
        verify(attributeRepository, atMostOnce()).save(notNull());


    }
}
