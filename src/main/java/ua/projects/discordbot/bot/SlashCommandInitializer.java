package ua.projects.discordbot.bot;

import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Component
public class SlashCommandInitializer {

    private final Hashtable<String, String> dataTable = new Hashtable<>();

    public List<SlashCommandOptionChoice> createCommandsOptions(List<String> inputDataList) {
        if (dataTable.keySet().size() != 0)
            dataTable.clear();
        for (String data : inputDataList) {
            dataTable.put(data, data.replace(" ", "_"));
        }
        List<SlashCommandOptionChoice> commandOptionChoiceList = new ArrayList<>();
        for (String key : dataTable.keySet()) {
            commandOptionChoiceList.add(SlashCommandOptionChoice.create(key, dataTable.get(key)));
        }
        return commandOptionChoiceList;
    }
}