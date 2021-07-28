package ua.projects.discordbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Attribute;
import ua.projects.discordbot.repository.AttributeRepository;
import ua.projects.discordbot.repository.CommonRepository;

import java.util.List;
import java.util.Set;

@Service
public class AttributeService extends CommonService implements CommonRepository<Attribute> {

    private static final Logger logger = LoggerFactory.getLogger(AttributeService.class);

    private final AttributeRepository repository;

    public AttributeService(AttributeRepository repository) {
        this.repository = repository;
    }

    //todo : check for isBlank before Hibernate do that. remove try catch. try throw exception if it necessary
    public Attribute create(String description) {
        Attribute attribute = new Attribute();
        try {
            if (notPresent(description)) {
                attribute = repository.save(new Attribute(description));
                updateCommands();
            }
        } catch (TransactionSystemException exception) {
            logger.error("Invalid input: " + exception.getMessage());
            throw new ValidationException("Description is mandatory. Description should be a string");
        }
        logger.debug("Attribute was created successfully");
        return attribute;
    }

    @Override
    public List<Attribute> findAll() {
        logger.debug("All attributes were found successfully");
        return repository.findAll();
    }

    //todo : check for null id before Hibernate do that. remove try catch. try throw exception if it necessary
    @Override
    public Attribute find(Integer id) {
        Attribute attribute;
        try {
            attribute = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException
                                    .notFoundException("Attribute with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Attribute was found successfully");
        return attribute;
    }

    //todo : check for null id and isBlank before Hibernate do that. remove try catch. try throw exception if it necessary
    public Attribute update(Integer id, String description) {
        Attribute attribute = find(id);
        try {
            if (notPresent(description))
                attribute.setDescription(description);
            repository.save(attribute);
            updateCommands();
        } catch (TransactionSystemException transactionSystemException) {
            logger.error("Invalid input: " + transactionSystemException.getMessage());
            throw new ValidationException("Description is mandatory. Description should be a string");
        }
        logger.debug("Attribute was updated successfully");
        return attribute;
    }

    //todo : change functionality. use one query to db
    @Override
    public void delete(Integer id) {
        Attribute attribute = find(id);
        repository.delete(attribute);
        updateCommands();
        logger.debug("Attribute was deleted successfully");
    }

    //todo : check for isBlank
    public Set<Attribute> getAttributesByName(String attributes) {
        String[] attributesToSearch = attributes.split(",\\s*");
        for (String description : attributesToSearch) {
            if (!repository.existsAttributeByDescriptionIs(description)) {
                logger.error("No attribute " + description);
                throw new EntityNotFoundException("Attribute " + description + " does absence in data base");
            }
        }
        logger.debug("All attributes were found successfully");
        return repository.findAttributesByDescriptionIn(attributesToSearch);
    }

    //todo : check for isBlank
    private boolean notPresent(String description) {
        if (repository.existsAttributeByDescriptionIs(description))
            throw new ValidationException("Description " + description + " presents in dataBase. Description should be unique.");
        return true;
    }
}
