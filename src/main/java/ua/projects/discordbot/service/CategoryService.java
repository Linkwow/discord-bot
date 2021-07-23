package ua.projects.discordbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import org.springframework.transaction.annotation.Transactional;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Category;
import ua.projects.discordbot.repository.CategoryRepository;
import ua.projects.discordbot.repository.CommonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService extends CommonService implements CommonRepository<Category> {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category create(String unitCategory) {
        Category category = new Category();
        try {
            if (notPresent(unitCategory)) {
                category = repository.save(new Category(unitCategory));
                updateCommands();
            }
        } catch (TransactionSystemException exception) {
            logger.error("Invalid input: " + exception.getMessage());
            throw new ValidationException("Category is mandatory. Category should be a string");
        }
        logger.debug("Category was created successfully");
        return category;
    }

    @Override
    public List<Category> findAll() {
        logger.debug("All categories were found successfully");
        return repository.findAll();
    }

    @Override
    public Category find(Integer id) {
        Category category;
        try {
            category = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException
                                    .notFoundException("Category with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Category was found successfully");
        return category;
    }

    public Category update(Integer id, String unitCategory) {
        Category category = find(id);
        try {
            if (notPresent(unitCategory))
                category.setUnitCategory(unitCategory);
            repository.save(category);
            updateCommands();
        } catch (TransactionSystemException transactionSystemException) {
            logger.error("Invalid input: " + transactionSystemException.getMessage());
            throw new ValidationException("Category is mandatory. Category should be a string");
        }
        logger.debug("Category was updated successfully");
        return category;
    }

    @Override
    public void delete(Integer id) {
        Category category = find(id);
        repository.delete(category);
        updateCommands();
        logger.debug("Category was deleted successfully");
    }

    public Category getCategoryByName(String name) {
        return Optional.ofNullable(repository.findCategoryByUnitCategoryIs(name))
                .orElseThrow(
                        () -> EntityNotFoundException.notFoundException("Category with name " + name + " does absence in data base"));
    }

    private boolean notPresent(String unitCategory) {
        if (repository.existsCategoryByUnitCategoryIs(unitCategory))
            throw new ValidationException("Unit category " + unitCategory + " presents in dataBase. Unit category should be unique.");
        return true;
    }
}
