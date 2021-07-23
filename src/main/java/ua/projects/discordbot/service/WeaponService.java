package ua.projects.discordbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Weapon;
import ua.projects.discordbot.repository.CommonRepository;
import ua.projects.discordbot.repository.WeaponRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WeaponService extends CommonService implements CommonRepository<Weapon> {

    private static final Logger logger = LoggerFactory.getLogger(WeaponService.class);

    private final WeaponRepository repository;

    public WeaponService(WeaponRepository repository) {
        this.repository = repository;
    }

    public Weapon create(String type){
        Weapon weapon = new Weapon();
        try {
            if (notPresent(type)) {
                weapon = repository.save(new Weapon(type));
                updateCommands();
            }
        } catch (TransactionSystemException exception) {
            logger.error("Invalid input: " + exception.getMessage());
            throw new ValidationException("Weapon type is mandatory. Weapon type should be a string");
        }
        logger.debug("Weapon was created successfully");
        return weapon;
    }

    @Override
    public List<Weapon> findAll() {
        logger.debug("All weapons type were found successfully");
        return repository.findAll();
    }

    @Override
    public Weapon find(Integer id) {
        Weapon weapon;
        try {
            weapon = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException
                                    .notFoundException("Weapon type with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Race was found successfully");
        return weapon;
    }

    public Weapon update(Integer id, String type){
        Weapon weapon = find(id);
        try {
            if (notPresent(type))
                weapon.setType(type);
            repository.save(weapon);
            updateCommands();
        } catch (TransactionSystemException transactionSystemException) {
            logger.error("Invalid input: " + transactionSystemException.getMessage());
            throw new ValidationException("Weapon type is mandatory. Weapon type should be a string");
        }
        logger.debug("Weapon was updated successfully");
        return weapon;
    }

    @Override
    public void delete(Integer id) {
        Weapon weapon = find(id);
        repository.delete(weapon);
        updateCommands();
        logger.debug("Weapon was deleted successfully");
    }

    public Weapon getWeaponByType(String name) {
        return Optional.ofNullable(repository.findWeaponByTypeIs(name))
                .orElseThrow(
                        () -> EntityNotFoundException.notFoundException("Weapon with type " + name + " does absence in data base"));
    }

    private boolean notPresent(String type) {
        if (repository.existsWeaponByTypeIs(type))
            throw new ValidationException("Weapon " + type + " presents in dataBase. Weapon should be unique.");
        return true;
    }
}
