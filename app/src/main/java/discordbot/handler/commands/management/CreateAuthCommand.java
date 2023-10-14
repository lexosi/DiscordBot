package discordbot.handler.commands.management;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.MessageCreateSpec;

import discordbot.handler.DiscordCommand;
import discordbot.handler.components.OpenAuthModalButton;
import discordbot.manager.discord.ComponentManager;

public class CreateAuthCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        final Button BUTTON = (Button) ComponentManager.INSTANCE.getValue(OpenAuthModalButton.ID).provide();

        event.getInteraction().getChannel().block().createMessage(MessageCreateSpec.builder()
                .content("Bienvenido al servidor de estudiantes de **IT Academy**.")
                .addComponent(ActionRow.of(BUTTON)).build())
                .block();

        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content("El mensaje se ha enviado con éxito.")
                .ephemeral(true)
                .build()).block();
    }

    @Override
    public String getCommandDescription() {
        return "Create a custom message to give roles to the people of the server.";
    }

}
