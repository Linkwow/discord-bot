package ua.projects.discordbot.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_generator")
    @SequenceGenerator(name = "categories_generator", sequenceName = "categories_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Integer id;

    @NotBlank(message = "unitCategory is mandatory")
    @Column(name = "unit_category")
    private String unitCategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Unit> unitList = new ArrayList<>();

    public Category() {
    }

    public Category(String unitCategory) {
        this.unitCategory = unitCategory;
    }

    public Integer getId() {
        return id;
    }

    public String getUnitCategory() {
        return unitCategory;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitCategory(String unitCategory) {
        this.unitCategory = unitCategory;
    }

    @SuppressWarnings("unused")
    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
