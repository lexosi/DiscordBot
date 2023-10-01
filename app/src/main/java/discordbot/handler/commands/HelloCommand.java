package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discordbot.handler.DiscordCommand;

public class HelloCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event) {
        event.reply("Hello World").block();
    }
}