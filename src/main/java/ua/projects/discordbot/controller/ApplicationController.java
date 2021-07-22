package ua.projects.discordbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ua.projects.discordbot.persistence.*;
import ua.projects.discordbot.service.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/total-war-warhammer")
public class ApplicationController {

    private AttributeService attributeService;

    private CategoryService categoryService;

    private FactionService factionService;

    private RaceService raceService;

    private UnitService unitService;

    private WeaponService weaponService;

    @Autowired
    public void setAttributeService(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setFactionService(FactionService factionService) {
        this.factionService = factionService;
    }

    @Autowired
    public void setRaceService(RaceService raceService) {
        this.raceService = raceService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    @Autowired
    public void setWeaponService(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/showAllUnitsFromChosenFaction")
    public ModelAndView getUnitsOfFaction(
            @Valid @RequestParam(name = "faction") String faction,
            @Valid @RequestParam(name = "category") String category) {
        Map<String, Object> units = new HashMap<>();
        units.put("units", unitService.getUnitsByFactionAndCategory(
                faction.replace("_", " "),
                category.replace("_", " ")));
        return new ModelAndView("getAllUnits", units);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/showAllFactionsFromChosenRace")
    public ModelAndView getFactionsOfRace(
            @Valid @RequestParam(name = "race") String race) {
        Map<String, Object> factions = new HashMap<>();
        factions.put("units", factionService.getFactionsByRace(
                race.replace("_", " ")));
        return new ModelAndView("getAllFactions", factions);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createAttribute")
    public ModelAndView createAttribute(@Valid @RequestParam(name = "attributeName") String description) {
        ModelAndView modelAndView = new ModelAndView("createAttribute");
        Attribute attribute = attributeService.create(description);
        modelAndView.addObject("id", attribute.getId());
        modelAndView.addObject("description", attribute.getDescription());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createCategory")
    public ModelAndView createCategory(@RequestParam(name = "unitCategory") String unitCategory) {
        ModelAndView modelAndView = new ModelAndView("createCategory");
        Category category = categoryService.create(unitCategory);
        modelAndView.addObject("id", category.getId());
        modelAndView.addObject("unitCategory", category.getUnitCategory());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createFaction")
    public ModelAndView createFaction(@RequestParam(name = "factionName") String factionName, @RequestParam(name = "raceName") String raceName) {
        ModelAndView modelAndView = new ModelAndView("createFaction");
        Faction faction = factionService.create(factionName, raceName);
        modelAndView.addObject("id", faction.getId());
        modelAndView.addObject("name", faction.getName());
        modelAndView.addObject("race", faction.getRace());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createRace")
    public ModelAndView createRace(@RequestParam(name = "raceName") String name) {
        ModelAndView modelAndView = new ModelAndView("createRace");
        Race race = raceService.create(name);
        modelAndView.addObject("id", race.getId());
        modelAndView.addObject("name", race.getName());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createUnit")
    public ModelAndView createUnit(@RequestParam(name = "name") String name,
                                   @RequestParam(name = "factionName") String factionName,
                                   @RequestParam(name = "unitCategory") String unitCategory,
                                   @RequestParam(name = "weaponType") String weaponType,
                                   @RequestParam(name = "attributes", required = false) String attributes,
                                   @RequestParam(name = "cost", required = false, defaultValue = "0") Integer cost,
                                   @RequestParam(name = "upkeep", required = false, defaultValue = "0") Integer upkeep,
                                   @RequestParam(name = "health", required = false, defaultValue = "0") Integer health,
                                   @RequestParam(name = "leadership", required = false, defaultValue = "0") Integer leadership,
                                   @RequestParam(name = "speed", required = false, defaultValue = "0") Integer speed,
                                   @RequestParam(name = "meleeAttack", required = false, defaultValue = "0") Integer meleeAttack,
                                   @RequestParam(name = "meleeDefence", required = false, defaultValue = "0") Integer meleeDefence,
                                   @RequestParam(name = "chargeBonus", required = false, defaultValue = "0") Integer chargeBonus,
                                   @RequestParam(name = "missileResistance", required = false, defaultValue = "0") Integer missileResistance,
                                   @RequestParam(name = "magicResistance", required = false, defaultValue = "0") Integer magicResistance,
                                   @RequestParam(name = "armorProtection", required = false, defaultValue = "0") Integer armorProtection,
                                   @RequestParam(name = "weaponDamage", required = false, defaultValue = "0") Integer weaponDamage,
                                   @RequestParam(name = "armourPiercingDamage", required = false, defaultValue = "0") Integer armourPiercingDamage,
                                   @RequestParam(name = "meleeInterval", required = false, defaultValue = "0") Integer meleeInterval,
                                   @RequestParam(name = "magicalAttack", required = false, defaultValue = "0") Integer magicalAttack,
                                   @RequestParam(name = "range", required = false, defaultValue = "0") Integer range,
                                   @RequestParam(name = "unitSize", required = false, defaultValue = "0") Integer unitSize,
                                   @RequestParam(name = "turns", required = false, defaultValue = "0") Integer turns) {
        Map<String, Integer> parameters = createParametersMap(
                cost, upkeep, health, leadership, speed, meleeAttack,
                meleeDefence, chargeBonus, missileResistance,
                magicResistance, armorProtection, weaponDamage,
                armourPiercingDamage, meleeInterval, magicalAttack, range, unitSize, turns);
        Unit unit = unitService.create(name, factionName, unitCategory, weaponType, attributes, parameters);
        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("unit", unit);
        unitMap.put("attributes", unit.getAttributeSet());
        return new ModelAndView("createUnit", unitMap);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/createWeapon")
    public ModelAndView createWeapon(@RequestParam(name = "type") String type) {
        ModelAndView modelAndView = new ModelAndView("createWeapon");
        Weapon weapon = weaponService.create(type);
        modelAndView.addObject("id", weapon.getId());
        modelAndView.addObject("type", weapon.getType());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getAttributes")
    public ModelAndView getAttributes() {
        Map<String, Object> allAttributes = new HashMap<>();
        allAttributes.put("attributes", attributeService.findAll());
        return new ModelAndView("getAllAttributes", allAttributes);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getCategories")
    public ModelAndView getCategories() {
        Map<String, Object> allCategories = new HashMap<>();
        allCategories.put("categories", categoryService.findAll());
        return new ModelAndView("getAllCategories", allCategories);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getFactions")
    public ModelAndView getFactions() {
        Map<String, Object> allFactions = new HashMap<>();
        allFactions.put("factions", factionService.findAll());
        return new ModelAndView("getAllFactions", allFactions);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getRaces")
    public ModelAndView getRaces() {
        Map<String, Object> allRaces = new HashMap<>();
        allRaces.put("races", raceService.findAll());
        return new ModelAndView("getAllRaces", allRaces);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getUnits")
    public ModelAndView getUnits() {
        Map<String, Object> allUnits = new HashMap<>();
        allUnits.put("units", unitService.findAll());
        return new ModelAndView("getAllUnits", allUnits);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getWeapons")
    public ModelAndView getWeapons() {
        Map<String, Object> allWeapons = new HashMap<>();
        allWeapons.put("weapons", weaponService.findAll());
        return new ModelAndView("getAllWeapons", allWeapons);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getAttributeById")
    public ModelAndView getAttribute(@RequestParam(name = "id") Integer id) {
        Attribute attribute = attributeService.find(id);
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("attributes", attribute);
        return new ModelAndView("getAllAttributes", attributeMap);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getCategoryById")
    public ModelAndView getCategory(@RequestParam(name = "id") Integer id) {
        Category category = categoryService.find(id);
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("categories", category);
        return new ModelAndView("getAllCategories", categoryMap);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getFactionById")
    public ModelAndView getFaction(@RequestParam(name = "id") Integer id) {
        Faction faction = factionService.find(id);
        Map<String, Object> factionMap = new HashMap<>();
        factionMap.put("factions", faction);
        return new ModelAndView("getAllFactions", factionMap);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getRaceById")
    public ModelAndView getRace(@RequestParam(name = "id") Integer id) {
        Race race = raceService.find(id);
        Map<String, Object> raceMap = new HashMap<>();
        raceMap.put("races", race);
        return new ModelAndView("getAllRaces", raceMap);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getUnitById")
    public ModelAndView getUnit(@RequestParam(name = "id") Integer id) {
        Unit unit = unitService.find(id);
        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("units", unit);
        return new ModelAndView("getAllUnits", unitMap);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/getWeaponById")
    public ModelAndView getWeapon(@RequestParam(name = "id") Integer id) {
        Weapon weapon = weaponService.find(id);
        Map<String, Object> weaponMap = new HashMap<>();
        weaponMap.put("weapons", weapon);
        return new ModelAndView("getAllWeapons", weaponMap);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateAttribute")
    public void updateAttribute(@RequestParam(name = "id") Integer id, @RequestParam(name = "description") String description) {
        attributeService.update(id, description);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateCategory")
    public void updateCategory(@RequestParam(name = "id") Integer id, @RequestParam(name = "unitCategory") String unitCategory) {
        categoryService.update(id, unitCategory);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateFaction")
    public void updateFaction(
            @RequestParam(name = "id") Integer id,
            @RequestParam(name = "factionName", required = false) String factionName,
            @RequestParam(name = "raceName", required = false) String raceName) {
        factionService.update(id, factionName, raceName);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateRace")
    public void updateRace(@RequestParam(name = "id") Integer id, @RequestParam(name = "name") @NotBlank String name) {
        raceService.update(id, name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateUnit")
    public void updateUnit(@RequestParam(name = "id") Integer id,
                           @RequestParam(name = "name", required = false) String name,
                           @RequestParam(name = "factionName", required = false) String factionName,
                           @RequestParam(name = "unitCategory", required = false) String unitCategory,
                           @RequestParam(name = "weaponType", required = false) String weaponType,
                           @RequestParam(name = "attributes", required = false) String attributes,
                           @RequestParam(name = "cost", required = false, defaultValue = "0") Integer cost,
                           @RequestParam(name = "upkeep", required = false, defaultValue = "0") Integer upkeep,
                           @RequestParam(name = "health", required = false, defaultValue = "0") Integer health,
                           @RequestParam(name = "leadership", required = false, defaultValue = "0") Integer leadership,
                           @RequestParam(name = "speed", required = false, defaultValue = "0") Integer speed,
                           @RequestParam(name = "meleeAttack", required = false, defaultValue = "0") Integer meleeAttack,
                           @RequestParam(name = "meleeDefence", required = false, defaultValue = "0") Integer meleeDefence,
                           @RequestParam(name = "chargeBonus", required = false, defaultValue = "0") Integer chargeBonus,
                           @RequestParam(name = "missileResistance", required = false, defaultValue = "0") Integer missileResistance,
                           @RequestParam(name = "magicResistance", required = false, defaultValue = "0") Integer magicResistance,
                           @RequestParam(name = "armorProtection", required = false, defaultValue = "0") Integer armorProtection,
                           @RequestParam(name = "weaponDamage", required = false, defaultValue = "0") Integer weaponDamage,
                           @RequestParam(name = "armourPiercingDamage", required = false, defaultValue = "0") Integer armourPiercingDamage,
                           @RequestParam(name = "meleeInterval", required = false, defaultValue = "0") Integer meleeInterval,
                           @RequestParam(name = "magicalAttack",  required = false, defaultValue = "0") Integer magicalAttack,
                           @RequestParam(name = "range", required = false, defaultValue = "0") Integer range,
                           @RequestParam(name = "unitSize", required = false, defaultValue = "0") Integer unitSize,
                           @RequestParam(name = "turns", required = false, defaultValue = "0") Integer turns) {
        Map<String, Integer> parameters = createParametersMap(
                cost, upkeep, health, leadership, speed, meleeAttack,
                meleeDefence, chargeBonus, missileResistance,
                magicResistance, armorProtection, weaponDamage,
                armourPiercingDamage, meleeInterval, magicalAttack, range, unitSize, turns);
        unitService.update(id, name, factionName, unitCategory, weaponType, attributes, parameters);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/admin/updateWeapon")
    public void updateWeapon(@RequestParam(name = "id") Integer id, @RequestParam(name = "name") String name) {
        weaponService.update(id, name);
    }

    @DeleteMapping("/admin/deleteAttribute")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttribute(@RequestParam(name = "id") Integer id) {
        attributeService.delete(id);
    }

    @DeleteMapping("/admin/deleteCategory")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@RequestParam(name = "id") Integer id) {
        categoryService.delete(id);
    }

    @DeleteMapping("/admin/deleteFaction")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFaction(@RequestParam(name = "id") Integer id) {
        factionService.delete(id);
    }


    @DeleteMapping("/admin/deleteRace")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRace(@RequestParam(name = "id") Integer id) {
        raceService.delete(id);
    }

    @DeleteMapping("/admin/deleteUnit")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUnit(@RequestParam(name = "id") Integer id) {
        unitService.delete(id);
    }

    @DeleteMapping("/admin/deleteWeapon")
    @ResponseStatus(HttpStatus.OK)
    public void deleteWeapon(@RequestParam(name = "id") Integer id) {
        weaponService.delete(id);
    }

    private Map<String, Integer> createParametersMap(Integer cost, Integer upkeep, Integer health, Integer leadership, Integer speed,
                                                     Integer meleeAttack, Integer meleeDefence, Integer chargeBonus, Integer missileResistance,
                                                     Integer magicResistance, Integer armorProtection, Integer weaponDamage, Integer armourPiercingDamage,
                                                     Integer meleeInterval, Integer magicalAttack, Integer range, Integer unitSize, Integer turns) {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("setCost", cost);
        parameters.put("setUpkeep", upkeep);
        parameters.put("setHealth", health);
        parameters.put("setLeadership", leadership);
        parameters.put("setSpeed", speed);
        parameters.put("setMeleeAttack", meleeAttack);
        parameters.put("setMeleeDefence", meleeDefence);
        parameters.put("setChargeBonus", chargeBonus);
        parameters.put("setMissileResistance", missileResistance);
        parameters.put("setMagicResistance", magicResistance);
        parameters.put("setArmorProtection", armorProtection);
        parameters.put("setWeaponDamage", weaponDamage);
        parameters.put("setArmourPiercingDamage", armourPiercingDamage);
        parameters.put("setMeleeInterval", meleeInterval);
        parameters.put("setMagicalAttack", magicalAttack);
        parameters.put("setRange", range);
        parameters.put("setUnitSize", unitSize);
        parameters.put("setTurns", turns);
        return parameters;
    }
}
