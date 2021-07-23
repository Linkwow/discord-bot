package ua.projects.discordbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Race;
import ua.projects.discordbot.repository.CommonRepository;
import ua.projects.discordbot.repository.RaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RaceService implements CommonRepository<Race> {

    private static final Logger logger = LoggerFactory.getLogger(RaceService.class);

    private final RaceRepository repository;

    public RaceService(RaceRepository repository) {
        this.repository = repository;
    }

    public Race create(String name) {
        Race race = new Race();
        try {
            if (notPresent(name)) {
                race = repository.save(new Race(name));
            }
        } catch (TransactionSystemException exception) {
            logger.error("Invalid input: " + exception.getMessage());
            throw new ValidationException("Race name is mandatory. Race name should be a string");
        }
        logger.debug("Race was created successfully");
        return race;
    }

    @Override
    public List<Race> findAll() {
        logger.debug("All races were found successfully");
        return repository.findAll();
    }

    @Override
    public Race find(Integer id) {
        Race race;
        try {
            race = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException.notFoundException("Race with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Race was found successfully");
        return race;
    }

    public Race update(Integer id, String name) {
        Race race = find(id);
        try {
            if (notPresent(name))
                race.setName(name);
            repository.save(race);
        } catch (TransactionSystemException transactionSystemException) {
            logger.error("Invalid input: " + transactionSystemException.getMessage());
            throw new ValidationException("Race name is mandatory. Race name should be a string");
        }
        logger.debug("Race was updated successfully");
        return race;
    }

    @Override
    public void delete(Integer id) {
        Race race = find(id);
        repository.delete(race);
        logger.debug("Race was deleted successfully");
    }

    public Race getRaceByName(String name) {
        return Optional.ofNullable(repository.findRaceByNameIs(name))
                .orElseThrow(
                        () -> EntityNotFoundException.notFoundException("Race with name " + name + " does absence in data base"));
    }

    private boolean notPresent(String name) {
        if (repository.existsRaceByNameIs(name))
            throw new ValidationException("Race " + name + " presents in dataBase. Race should be unique.");
        return true;
    }
}
