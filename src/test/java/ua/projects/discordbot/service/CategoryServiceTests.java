package ua.projects.discordbot.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void testContextLoads() {
        assertNotNull(categoryService);
    }

    @Test
    @Order(1)
    public void createCategory() {
        String unitCategory = "new Category";
        Category testEntity = new Category(unitCategory);
        Category modelEntity = categoryService.create(unitCategory);
        Assertions.assertEquals(modelEntity.getUnitCategory(), testEntity.getUnitCategory());
    }

    @Test
    @Order(2)
    void exceptionWhenCreate() {
        Exception emptyUnitCategory = assertThrows(ValidationException.class,
                () -> categoryService.create(null));
        String expectedMessage = "Category is mandatory. Category should be a string";
        String actualMessage = emptyUnitCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        String unitCategory = "new Category";
        Exception duplicateUnitCategory = assertThrows(ValidationException.class,
                () -> categoryService.create(unitCategory));
        expectedMessage = "Unit category " + unitCategory + " presents in dataBase. Unit category should be unique.";
        actualMessage = duplicateUnitCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void findAllCategories() {
        categoryService.create("first");
        List<Category> categories = categoryService.findAll();
        Assertions.assertTrue(categories.size() > 0);
    }

    @Test
    @Order(4)
    void findCategoryById() {
        Category categoryForId = categoryService.create("second");
        Category category = categoryService.find(categoryForId.getId());
        Assertions.assertEquals("second", category.getUnitCategory());
    }

    @Test
    @Order(5)
    void exceptionWhenFind() {
        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> categoryService.find(0));
        String expectedMessage = "Category with id " + id + " not found";
        String actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Exception nullId = assertThrows(ValidationException.class,
                () -> categoryService.find(null));
        expectedMessage = "Id is mandatory. Id should be a number.";
        actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void updateCategoryById() {
        Category beforeUpdate = categoryService.create("beforeUpdate");
        Category afterUpdate = categoryService.update(beforeUpdate.getId(), "afterUpdate");
        Assertions.assertEquals("afterUpdate", afterUpdate.getUnitCategory());
    }

    @Test
    @Order(6)
    void exceptionWhenUpdate() {
        Exception nullId = assertThrows(ValidationException.class,
                () -> categoryService.update(null, "without id but with unit category"));
        String expectedMessage = "Id is mandatory. Id should be a number.";
        String actualMessage = nullId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Category categoryForUpdate = categoryService.create("update");
        Exception nullUnitCategory = assertThrows(ValidationException.class,
                () -> categoryService.update(categoryForUpdate.getId(), null));
        expectedMessage = "Category is mandatory. Category should be a string";
        actualMessage = nullUnitCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        int id = 0;
        Exception wrongId = assertThrows(EntityNotFoundException.class,
                () -> categoryService.find(0));
        expectedMessage = "Category with id " + id + " not found";
        actualMessage = wrongId.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Category simpleCategory = categoryService.create("duplicate again");
        String unitCategory = "duplicate again";
        Exception duplicateUnitCategory = assertThrows(ValidationException.class,
                () -> categoryService.update(simpleCategory.getId(), unitCategory));
        expectedMessage = "Unit category " + unitCategory + " presents in dataBase. Unit category should be unique.";
        actualMessage = duplicateUnitCategory.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(7)
    void deleteCategoryById() {
        Category categoryForDelete = categoryService.create("delete");
        int before = categoryService.findAll().size();
        categoryService.delete(categoryForDelete.getId());
        int after = categoryService.findAll().size();
        Assertions.assertEquals(after, (before - 1));
    }

    @Test
    @Order(8)
    void getCategoryByName() {
        String categoryName = "Test";
        categoryService.create(categoryName);
        Category read = categoryService.getCategoryByName(categoryName);
        Assertions.assertEquals(categoryName, read.getUnitCategory());
    }

    @Test
    @Order(9)
    void getCategoryByNameWithException(){
        String name = "no such category";
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getCategoryByName(name));
        String expectedMessage = "Category with name " + name + " does absence in data base";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
