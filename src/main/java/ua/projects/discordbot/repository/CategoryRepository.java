package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.projects.discordbot.persistence.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findCategoryByUnitCategoryIs(String unitCategory);

    boolean existsCategoryByUnitCategoryIs(String unitCategory);

}
