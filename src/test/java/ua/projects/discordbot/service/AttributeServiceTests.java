package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Attribute;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

//todo : change all service test from integration to unit. all test should be independent from each other
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttributeServiceTests {

    @Autowired
    private AttributeService attributeService;

    //todo : change for something needed
    @BeforeEach
    void testContextLoads() {
        assertNotNull(attributeService);
    }

    @Test
    @Order(1)
    public void createAttribute() {
        String description = "new Attribute";
        Attribute testEntity = new Attribute(description);
        Attribute modelEntity = attributeService.create(description);
        Assertions.assertEquals(modelEntity.getDescription(), testEntity.getDescription());
    }

    @Test
    @Order(2)
    void exceptionWhenCreate() {
        Exception emptyDescription = assertThrows(ValidationException.class,
                () -> attributeService.create(null));
        String expectedMessage = "Description is mandatory. Description should be a string";
        String actualMessage = emptyDescription.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String description = "new Attribute";
        Exception duplicateDescription = assertThrows(ValidationException.class,
                () -> attributeService.create(description));
        expectedMessage = "Description " + description + " presents in dataBase. Description should be unique.";
        actualMessage = duplicateDescription.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAllAttributes() {
        attributeService.create("first");
        List<Attribute> attributes = attributeService.findAll();
        Assertions.assertTrue(attributes.size() > 0);
    }

    @Test
    @Order(4)
    void findAttributeById() {
        Attribute attributeForId = attributeService.create("second");
        Attribute attribute = attributeService.find(attributeForId.getId());
        Assertions.assertEquals("second", attribute.getDescription());
    }

    @Test
    @Order(5)
    void exceptionWhenFind() {
        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> attributeService.find(0));
        String expectedMessage = "Attribute with id " + id + " not found";
        String actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> attributeService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void updateAttributeById() {
        Attribute beforeUpdate = attributeService.create("beforeUpdate");
        Attribute afterUpdate = attributeService.update(beforeUpdate.getId(), "afterUpdate");
        Assertions.assertEquals("afterUpdate", afterUpdate.getDescription());
    }

    @Test
    @Order(6)
    void exceptionWhenUpdate() {
        Exception nullId = assertThrows(ValidationException.class,
                () -> attributeService.update(null, "without id but with description"));
        String expectedMessage = "Id is mandatory. Id should be a number.";
        String actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Attribute attributeForUpdate = attributeService.create("update");
        Exception nullDescription = assertThrows(ValidationException.class,
                () -> attributeService.update(attributeForUpdate.getId(), null));
        expectedMessage = "Description is mandatory. Description should be a string";
        actualMessage = nullDescription.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> attributeService.find(0));
        expectedMessage = "Attribute with id " + id + " not found";
        actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Attribute simpleAttribute = attributeService.create("duplicate again");
        String description = "duplicate again";
        Exception duplicateDescription = assertThrows(ValidationException.class,
                () -> attributeService.update(simpleAttribute.getId(), description));
        expectedMessage = "Description " + description + " presents in dataBase. Description should be unique.";
        actualMessage = duplicateDescription.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void deleteAttributeById() {
        Attribute attributeForDelete = attributeService.create("delete");
        int before = attributeService.findAll().size();
        attributeService.delete(attributeForDelete.getId());
        int after = attributeService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void getAttributesByName() {
        attributeService.create("1");
        attributeService.create("2");
        attributeService.create("3");
        String attributes = "1,2,  3";
        Set<Attribute> attributeSet = attributeService.getAttributesByName(attributes);
        Assertions.assertEquals(3, attributeSet.size());
    }

    @Test
    @Order(9)
    void exceptionWhenGetAttributesByName() {
        String attributes = "no such attribute";
        Exception wrongAttribute = assertThrows(EntityNotFoundException.class,
                () -> attributeService.getAttributesByName(attributes));
        String expectedMessage = "Attribute no such attribute does absence in data base";
        String actualMessage = wrongAttribute.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
