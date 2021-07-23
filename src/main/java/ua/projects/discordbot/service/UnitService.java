package ua.projects.discordbot.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.projects.discordbot.exceptions.EntityNotFoundException;
import ua.projects.discordbot.exceptions.ValidationException;
import ua.projects.discordbot.persistence.*;
import ua.projects.discordbot.repository.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UnitService extends CommonService implements CommonRepository<Unit> {

    private static final Logger logger = LoggerFactory.getLogger(UnitService.class);

    private final UnitRepository repository;

    private FactionService factionService;

    private CategoryService categoryService;

    private WeaponService weaponService;

    private AttributeService attributeService;

    public UnitService(UnitRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFactionService(FactionService factionService) {
        this.factionService = factionService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setWeaponService(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @Autowired
    public void setAttributeService(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    public Unit create(String name, String factionName, String unitCategory, String weaponType, String attributes, Map<String, Integer> parameters) {
        Faction faction = factionService.getFactionByName(Optional.ofNullable(factionName).orElseThrow(
                () -> ValidationException.notValid("Faction name is mandatory.")));
        Category category = categoryService.getCategoryByName(Optional.ofNullable(unitCategory).orElseThrow(
                () -> ValidationException.notValid("Unit category is mandatory.")));
        Weapon weapon = weaponService.getWeaponByType(Optional.ofNullable(weaponType).orElseThrow(
                () -> ValidationException.notValid("Weapon type is mandatory.")));
        Set<Attribute> attributeSet = attributeService.getAttributesByName(Optional.ofNullable(attributes).orElseThrow(
                () -> ValidationException.notValid("Attributes name is mandatory.")));
        Unit unit = new Unit();
        try {
            notPresent(Optional.ofNullable(name).orElseThrow(
                    () -> ValidationException.notValid("Name is mandatory.")));
            unit.setName(name);
            unit.setFaction(faction);
            unit.setCategory(category);
            unit.setWeaponType(weapon);
            unit.setAttributeSet(attributeSet);
            setUnitParameters(unit, parameters);
            unit = repository.save(unit);
            updateCommands();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            logger.error("Error. Check the create method " + e);
            throw new RuntimeException("Error while invocation parameter set");
        }
        logger.debug("Unit was created successfully");
        return unit;
    }

    @Override
    public List<Unit> findAll() {
        logger.debug("All units were found successfully");
        return repository.findAll();
    }

    @Override
    public Unit find(Integer id) {
        Unit unit;
        try {
            unit = repository.findById(id)
                    .orElseThrow(
                            () -> EntityNotFoundException
                                    .notFoundException("Unit with id " + id + " not found"));
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            logger.error("Invalid input: " + invalidDataAccessApiUsageException.getMessage());
            throw new ValidationException("Id is mandatory. Id should be a number.");
        }
        logger.debug("Unit was found successfully");
        return unit;
    }

    public Unit update(Integer id, String name, String factionName, String unitCategory, String weaponType, String attributes, Map<String, Integer> parameters) {
        Unit unit = find(id);
        unit.setName(Optional.ofNullable(name).orElse(unit.getName()));
        if (factionName != null)
            unit.setFaction(factionService.getFactionByName(factionName));
        if (unitCategory != null)
            unit.setCategory(categoryService.getCategoryByName(unitCategory));
        if (weaponType != null)
            unit.setWeaponType(weaponService.getWeaponByType(weaponType));
        if (attributes != null)
            unit.setAttributeSet(attributeService.getAttributesByName(attributes));
        try {
            if (parameters != null)
                setUnitParameters(unit, parameters);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            logger.error("Error. Check the update method " + e);
            throw new RuntimeException("Error while invocation parameter set");
        }
        logger.debug("Unit was updated successfully");
        updateCommands();
        return unit;
    }

    @Override
    public void delete(Integer id) {
        Unit unit = find(id);
        repository.delete(unit);
        updateCommands();
        logger.debug("Unit was deleted successfully");
    }

    public List<Unit> getUnitsByFactionAndCategory(String faction, String category) {
        return repository.getUnitsByFactionAndCategory(faction, category);
    }

    private void setUnitParameters(Unit unit, Map<String, Integer> parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (String methodName : parameters.keySet()) {
            if (parameters.get(methodName) != null) {
                Method method = unit.getClass().getMethod(methodName, Integer.class);
                method.invoke(unit, parameters.get(methodName));
            }
        }
    }

    private boolean notPresent(String name) {
        if (repository.existsUnitByNameIs(name))
            throw new ValidationException("Unit with " + name + " presents in dataBase. Unit name should be unique.");
        return true;
    }
}
