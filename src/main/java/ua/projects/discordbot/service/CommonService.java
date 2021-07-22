package ua.projects.discordbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.projects.discordbot.bot.SlashCommandCreator;

@Component
public class CommonService {

    private SlashCommandCreator slashCommandCreator;

    @Autowired
    public void setSlashCommandCreator(SlashCommandCreator slashCommandCreator) {
        this.slashCommandCreator = slashCommandCreator;
    }

    public void updateCommands(){
        slashCommandCreator.updateCommands();
    }
}
