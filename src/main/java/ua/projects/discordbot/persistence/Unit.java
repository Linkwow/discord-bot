package ua.projects.discordbot.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import javax.validation.constraints.NotBlank;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "units_generator")
    @SequenceGenerator(name = "units_generator", sequenceName = "units_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotBlank(message = "name is mandatory")
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faction")
    private Faction faction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "upkeep")
    private Integer upkeep;

    @Column(name = "health")
    private Integer health;

    @Column(name = "leadership")
    private Integer leadership;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "melee_attack")
    private Integer meleeAttack;

    @Column(name = "melee_defence")
    private Integer meleeDefence;

    @Column(name = "charge_bonus")
    private Integer chargeBonus;

    @Column(name = "missile_resistance")
    private Integer missileResistance;

    @Column(name = "magic_resistance")
    private Integer magicResistance;

    @Column(name = "armor_protection")
    private Integer armorProtection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_type")
    private Weapon weaponType;

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "weapon_damage")
    private Integer weaponDamage;

    @Column(name = "armour_piercing_damage")
    private Integer armourPiercingDamage;

    @Column(name = "melee_interval")
    private Integer meleeInterval;

    @Column(name = "magical_attack")
    private Integer magicalAttack;

    @Column(name = "range")
    private Integer range;

    @Column(name = "unit_size")
    private Integer unitSize;

    @Column(name = "turns")
    private Integer turns;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "attributes_units",
            joinColumns = @JoinColumn(name = "unit_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id", referencedColumnName = "id"))

    private Set<Attribute> attributeSet = new HashSet<>();

    public Unit() {
    }

    public Unit(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Faction getFaction() {
        return faction;
    }

    public Category getCategory() {
        return category;
    }

    @SuppressWarnings("unused")
    public Integer getCost() {
        return cost;
    }

    @SuppressWarnings("unused")
    public Integer getUpkeep() {
        return upkeep;
    }

    @SuppressWarnings("unused")
    public Integer getHealth() {
        return health;
    }

    @SuppressWarnings("unused")
    public Integer getLeadership() {
        return leadership;
    }

    @SuppressWarnings("unused")
    public Integer getSpeed() {
        return speed;
    }

    @SuppressWarnings("unused")
    public Integer getMeleeAttack() {
        return meleeAttack;
    }

    @SuppressWarnings("unused")
    public Integer getMeleeDefence() {
        return meleeDefence;
    }

    @SuppressWarnings("unused")
    public Integer getChargeBonus() {
        return chargeBonus;
    }

    @SuppressWarnings("unused")
    public Integer getMissileResistance() {
        return missileResistance;
    }

    @SuppressWarnings("unused")
    public Integer getMagicResistance() {
        return magicResistance;
    }

    @SuppressWarnings("unused")
    public Integer getArmorProtection() {
        return armorProtection;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    @SuppressWarnings("unused")
    public Integer getWeaponDamage() {
        return weaponDamage;
    }

    @SuppressWarnings("unused")
    public Integer getArmourPiercingDamage() {
        return armourPiercingDamage;
    }

    @SuppressWarnings("unused")
    public Integer getMeleeInterval() {
        return meleeInterval;
    }

    @SuppressWarnings("unused")
    public Integer getMagicalAttack() {
        return magicalAttack;
    }

    @SuppressWarnings("unused")
    public Integer getRange() {
        return range;
    }

    @SuppressWarnings("unused")
    public Integer getUnitSize() {
        return unitSize;
    }

    @SuppressWarnings("unused")
    public Integer getTurns() {
        return turns;
    }

    public Set<Attribute> getAttributeSet() {
        return attributeSet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        faction.getUnitList().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getUnitList().add(this);
    }

    @SuppressWarnings("unused")
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @SuppressWarnings("unused")
    public void setUpkeep(Integer upkeep) {
        this.upkeep = upkeep;
    }

    @SuppressWarnings("unused")
    public void setHealth(Integer health) {
        this.health = health;
    }

    @SuppressWarnings("unused")
    public void setLeadership(Integer leadership) {
        this.leadership = leadership;
    }

    @SuppressWarnings("unused")
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setMeleeAttack(Integer meleeAttack) {
        this.meleeAttack = meleeAttack;
    }

    public void setMeleeDefence(Integer meleeDefence) {
        this.meleeDefence = meleeDefence;
    }

    @SuppressWarnings("unused")
    public void setChargeBonus(Integer chargeBonus) {
        this.chargeBonus = chargeBonus;
    }

    @SuppressWarnings("unused")
    public void setMissileResistance(Integer missileResistance) {
        this.missileResistance = missileResistance;
    }

    @SuppressWarnings("unused")
    public void setMagicResistance(Integer magicResistance) {
        this.magicResistance = magicResistance;
    }

    @SuppressWarnings("unused")
    public void setArmorProtection(Integer armorProtection) {
        this.armorProtection = armorProtection;
    }

    public void setWeaponType(Weapon weaponType) {
        this.weaponType = weaponType;
        weaponType.getUnitList().add(this);
    }

    @SuppressWarnings("unused")
    public void setWeaponDamage(Integer weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    @SuppressWarnings("unused")
    public void setArmourPiercingDamage(Integer armourPiercingDamage) {
        this.armourPiercingDamage = armourPiercingDamage;
    }

    @SuppressWarnings("unused")
    public void setMeleeInterval(Integer meleeInterval) {
        this.meleeInterval = meleeInterval;
    }

    @SuppressWarnings("unused")
    public void setMagicalAttack(Integer magicalAttack) {
        this.magicalAttack = magicalAttack;
    }

    @SuppressWarnings("unused")
    public void setRange(Integer range) {
        this.range = range;
    }

    @SuppressWarnings("unused")
    public void setUnitSize(Integer unitSize) {
        this.unitSize = unitSize;
    }

    @SuppressWarnings("unused")
    public void setTurns(Integer turns) {
        this.turns = turns;
    }

    public void setAttributeSet(Set<Attribute> attributeSet) {
        this.attributeSet = attributeSet;
        this.attributeSet.forEach((attribute) -> attribute.getUnitSet().add(this));
    }
}
