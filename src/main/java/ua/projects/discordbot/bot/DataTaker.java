package ua.projects.discordbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.projects.discordbot.persistence.Faction;
import ua.projects.discordbot.persistence.Race;
import ua.projects.discordbot.repository.FactionRepository;
import ua.projects.discordbot.repository.RaceRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataTaker {

    private RaceRepository raceRepository;

    private FactionRepository factionRepository;

    @Autowired
    public void setRaceRepository(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Autowired
    public void setFactionRepository(FactionRepository factionRepository) {
        this.factionRepository = factionRepository;
    }

    public DataTaker() {

    }

    //todo : change method to stream
    public List<String> getRacesFromDataBase() {
        List<String> data = new ArrayList<>();
        for (Race race : raceRepository.findAll()) {
            data.add(race.getName());
        }
        return data;
    }

    //todo : change method to stream
    public List<String> getFactionsFromDataBase() {
        List<String> data = new ArrayList<>();
        for (Faction faction : factionRepository.findAll()) {
            data.add(faction.getName());
        }
        return data;
    }
}