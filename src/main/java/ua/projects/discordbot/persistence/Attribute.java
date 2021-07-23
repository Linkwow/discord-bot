package ua.projects.discordbot.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "attributes")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attributes_generator")
    @SequenceGenerator(name = "attributes_generator", sequenceName = "attributes_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotBlank(message = "description is mandatory")
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "attributeSet", fetch = FetchType.LAZY)
    Set<Unit> unitSet = new HashSet<>();

    public Attribute() {

    }

    public Attribute(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Set<Unit> getUnitSet() {
        return unitSet;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public void setUnitSet(Set<Unit> unitSet) {
        this.unitSet = unitSet;
    }
}
