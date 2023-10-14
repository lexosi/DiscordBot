package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discordbot.handler.DiscordCommand;

public class IntranetCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content(
                        "Accede a la web de formaciones haciendo click [aquí](https://itacademy.barcelonactiva.cat/login/index.php)")
                .ephemeral(true)
                .build()).block();
    }

    @Override
    public String getCommandDescription() {
        return "Intranet Cybernarium";
    }

}
