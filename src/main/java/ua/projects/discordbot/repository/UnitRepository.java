package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.projects.discordbot.persistence.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

    @Query("select u from Unit u " +
            "join Faction f on f.id = u.faction.id and f.name = :faction " +
            "join Category c on c.id = u.category.id and c.unitCategory = :category")
    List<Unit> getUnitsByFactionAndCategory(String faction, String category);

    boolean existsUnitByNameIs(String name);

}
