package ua.projects.discordbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import org.springframework.transaction.annotation.Transactional;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.Faction;
import ua.projects.discordbot.persistence.Race;
import ua.projects.discordbot.repository.CommonRepository;
import ua.projects.discordbot.repository.FactionRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FactionService implements CommonRepository<Faction> {

    private static final Logger logger = LoggerFactory.getLogger(FactionService.class);

    private final FactionRepository repository;

    private RaceService raceService;

    public FactionService(FactionRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRaceService(RaceService raceService) {
        this.raceService = raceService;
    }

    public Faction create(String name, String raceName) {
        Faction faction = new Faction();
        try {
            if (notPresent(name))
                faction.setName(name);
            Race race = raceService.getRaceByName(Optional.ofNullable(raceName).orElseThrow(
                    () -> ValidationException.notValid("Race name is mandatory. Name should be a string")));
            faction.setRace(race);
            repository.save(faction);
        } catch (TransactionSystemException | ConstraintViolationException exception) {
            logger.error("Invalid input: " + exception.getMessage());
            throw new ValidationException("Name is mandatory. Name should be a string");
        }
        logger.debug("Faction was created successfully");
        return faction;
    }

    @Override
    public List<Faction> findAll() {
        logger.debug("All factions were found successfully");
        return repository.findAll();
    }

    @Override
    public Faction find(Integer id) {
        Faction faction;
        try {
            faction = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException
                                    .notFoundException("Faction with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Faction was found successfully");
        return faction;
    }

    public Faction update(Integer id, String name, String raceName) {
        Faction faction = find(id);
        if (notPresent(name))
            faction.setName(Optional.ofNullable(name).orElse(faction.getName()));
        if (!(raceName == null || raceName.isBlank())) {
            Race race = raceService.getRaceByName(raceName);
            faction.setRace(race);
        }
        repository.save(faction);
        logger.debug("Attribute was updated successfully");
        return faction;
    }

    @Override
    public void delete(Integer id) {
        Faction faction = find(id);
        repository.delete(faction);
        logger.debug("Faction was deleted successfully");
    }

    public List<Faction> getFactionsByRace(String raceName) {
        Race race = raceService.getRaceByName(raceName);
        List<Faction> test = repository.findFactionsByRace(race.getId());
        return test;
    }

    public Faction getFactionByName(String name) {
        return Optional.ofNullable(repository.findFactionByNameIs(name))
                .orElseThrow(
                        () -> EntityNotFoundException.notFoundException("Faction with name " + name + " does absence in data base"));
    }

    private boolean notPresent(String name) {
        if (repository.existsFactionByNameIs(name))
            throw new ValidationException("Faction " + name + " presents in dataBase. Faction should be unique.");
        return true;
    }
}
