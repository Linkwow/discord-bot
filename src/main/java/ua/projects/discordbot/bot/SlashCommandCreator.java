package ua.projects.discordbot.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SlashCommandCreator {

    private final DiscordApi discordApi;

    private final SlashCommandInitializer slashCommandInitializer;

    private final DataTaker dataTaker;

    private List<SlashCommandOptionChoice> races = new ArrayList<>();

    private List<SlashCommandOptionChoice> factions = new ArrayList<>();

    private final List<SlashCommandOptionChoice> units = new ArrayList<>(
            Arrays.asList(
                    SlashCommandOptionChoice.create("Lords", "Legendary_Lords"),
                    SlashCommandOptionChoice.create("Heroes", "Heroes"),
                    SlashCommandOptionChoice.create("Units", "Units")));

    private final List<SlashCommandOption> entities = new ArrayList<>();

    private final List<SlashCommandOption> racesAndFactions = new ArrayList<>();

    private void setFactions(List<String> inputData) {
        factions = slashCommandInitializer.createCommandsOptions(inputData);
    }

    private void setRaces(List<String> inputData) {
        races = slashCommandInitializer.createCommandsOptions(inputData);
    }

    private void setCommand() {
        SlashCommand.with("show-units", "shows units from chosen race and faction", getEntities()).createGlobal(discordApi).join();
        SlashCommand.with("show-factions", "shows factions from chosen race", getRacesAndFactions()).createGlobal(discordApi).join();
    }

    private void setEntities() {
        entities.clear();
        Collections.addAll(entities,
                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "race", "Choose race", true, races),
                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "faction", "Choose faction", true, factions),
                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "unit", "Choose unit", true, units));
    }

    private void setRacesAndFactions() {
        racesAndFactions.clear();
        Collections.addAll(racesAndFactions,
                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "race", "Choose race", true, races));
    }


    private List<SlashCommandOption> getEntities() {
        setEntities();
        return entities;
    }

    private List<SlashCommandOption> getRacesAndFactions() {
        setRacesAndFactions();
        return racesAndFactions;
    }

    @Autowired
    public SlashCommandCreator(DiscordApi discordApi, SlashCommandInitializer slashCommandInitializer, DataTaker dataTaker) {
        this.discordApi = discordApi;
        this.slashCommandInitializer = slashCommandInitializer;
        this.dataTaker = dataTaker;
    }

    public void updateCommands() {
        setRaces(dataTaker.getRacesFromDataBase());
        setFactions(dataTaker.getFactionsFromDataBase());
        setCommand();
    }
}