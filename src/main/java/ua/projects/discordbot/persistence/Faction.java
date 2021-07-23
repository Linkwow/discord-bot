package ua.projects.discordbot.persistence;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "factions")
public class Faction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factions_generator")
    @SequenceGenerator(name = "factions_generator", sequenceName = "factions_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotBlank(message = "name is mandatory")
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race")
    private Race race;


    @OneToMany(mappedBy = "faction", fetch = FetchType.LAZY)
    private List<Unit> unitList = new ArrayList<>();

    public Faction() {
    }

    public Faction(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Race getRace() {
        return race;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRace(Race race) {
        this.race = race;
        race.getFactionList().add(this);
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
