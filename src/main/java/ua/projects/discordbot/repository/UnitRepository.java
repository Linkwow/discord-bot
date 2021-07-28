package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.projects.discordbot.persistence.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

    @Query("select u, f, c, w from Unit u " +
            "join fetch Faction f on u.faction.id = f.id and f.name = :faction " +
            "join fetch Category c on u.category.id = c.id and c.unitCategory = :category " +
            "join fetch Weapon w on u.weaponType.id = w.id")
    List<Unit> getUnitsByFactionAndCategory(String faction, String category);

    boolean existsUnitByNameIs(String name);
}
