package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.projects.discordbot.persistence.Faction;

import java.util.List;

public interface FactionRepository extends JpaRepository<Faction, Integer> {

    Faction findFactionByNameIs(String factionName);

    @Query("select f, r from Faction f " +
            "join Race r on f.race.id = r.id and r.id = :id")
    List<Faction> findFactionsByRace(Integer id);

    boolean existsFactionByNameIs(String name);
}
