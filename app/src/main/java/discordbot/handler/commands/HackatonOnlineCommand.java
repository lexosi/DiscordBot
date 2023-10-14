package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discordbot.handler.DiscordCommand;

public class HackatonOnlineCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content(
                        "Accede a la web de los Hackatones que se hacen OnLine [aquí](https://www.eventbrite.es/d/online/hackathon/?page=1)")
                .ephemeral(true)
                .build()).block();
    }

    @Override
    public String getCommandDescription() {
        return "Hackatones OnLine";
    }

}