package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discordbot.handler.DiscordCommand;

public class GithubCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent discord, ApplicationCommandInteraction interaction) {
        final String USER = interaction.getOption("user").get().getValue().get().asString();

    }

    @Override
    public String getCommandDescription() {
        return "Know Github profile of the user you select";
    }

}
