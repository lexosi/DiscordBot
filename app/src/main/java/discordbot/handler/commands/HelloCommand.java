package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discordbot.handler.DiscordCommand;

public class HelloCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content("Hello World")
                .build()).block();
    }

    @Override
    public String getCommandDescription() {
        return "Says Hello World";
    }
}