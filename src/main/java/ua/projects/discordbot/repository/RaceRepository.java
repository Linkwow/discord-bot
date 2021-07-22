package ua.projects.discordbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.projects.discordbot.persistence.Race;

public interface RaceRepository extends JpaRepository<Race, Integer> {

    Race findRaceByNameIs(String racesName);

    boolean existsRaceByNameIs(String name);
}
