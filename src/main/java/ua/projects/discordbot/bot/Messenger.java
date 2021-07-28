package ua.projects.discordbot.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.interaction.SlashCommandInteraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Messenger {

    private final DiscordApi discordApi;

    private final SlashCommandCreator slashCommandCreator;

    private final StringBuilder sb = new StringBuilder();

    @Value("${my.server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private Integer serverPort;

    @Autowired
    public Messenger(DiscordApi discordApi, SlashCommandCreator slashCommandCreator) {
        this.discordApi = discordApi;
        this.slashCommandCreator = slashCommandCreator;
        start();
    }

    public void start() {
        slashCommandCreator.updateCommands();
        listenMainChannel();
        showUnits();
    }

    public void listenMainChannel() {
        discordApi.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("start")) {
                MessageAuthor messageAuthor = event.getMessageAuthor();
                discordApi.getUserById(messageAuthor.getId()).join().sendMessage("hi, " +
                        messageAuthor.getDisplayName() + ". Start your interaction with print \"/\"");
            }
        });
    }

    //todo : use some pattern to remove if else statement
    public void showUnits() {
        discordApi.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            String reference = "", race, faction, unitType;
            if (slashCommandInteraction.getCommandName().equals("show-units")) {
                faction = slashCommandInteraction.getSecondOptionStringValue().orElseThrow();
                unitType = slashCommandInteraction.getThirdOptionStringValue().orElseThrow();
                if (unitType.equals("Legendary Lords")) {
                    sb.append(unitType);
                    sb.deleteCharAt(sb.length() - 1);
                } else if (unitType.equals("Heroes")) {
                    sb.append(unitType);
                    sb.delete(4, 6);
                } else {
                    sb.append(unitType);
                    sb.deleteCharAt(sb.length() - 1);
                }
                reference = "http://" + serverAddress + ":" + serverPort + "/total-war-warhammer/user/showAllUnitsFromChosenFaction?" + "faction=" + faction + "&category=" + sb;
                sb.setLength(0);
            } else if (slashCommandInteraction.getCommandName().equals("show-factions")) {
                race = slashCommandInteraction.getFirstOptionStringValue().orElseThrow();
                reference = "http://" + serverAddress + ":" + serverPort + "/total-war-warhammer/user/showAllFactionsFromChosenRace?race=" + race;
            }
            slashCommandInteraction.createImmediateResponder().setContent(reference).respond();
        });
    }
}
