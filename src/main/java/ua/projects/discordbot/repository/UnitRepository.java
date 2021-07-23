package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.projects.discordbot.persistence.Unit;

import java.util.List;
@Transactional
public interface UnitRepository extends JpaRepository<Unit, Integer> {

  /* @Query("select u from Unit u " +
            "join fetch Faction f on u.faction.id = f.id and f.name = :faction " +
            "join fetch Category c on u.category.id = c.id and c.unitCategory = :category")*/
  @Query("select u, f, c, w from Unit u, Faction f, Category c, Weapon w " +
            "where f.name = :faction and c.unitCategory = :category")
    List<Unit> getUnitsByFactionAndCategory(String faction, String category);

    boolean existsUnitByNameIs(String name);
}
