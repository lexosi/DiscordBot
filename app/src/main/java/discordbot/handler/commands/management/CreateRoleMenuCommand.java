package discordbot.handler.commands.management;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.component.ActionRow;

import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.MessageCreateSpec;

import discordbot.handler.DiscordCommand;
import discordbot.handler.components.LanguageReactionRoleMenu;
import discordbot.manager.discord.ComponentManager;

public class CreateRoleMenuCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        final SelectMenu MENU = (SelectMenu) ComponentManager.INSTANCE.getValue(LanguageReactionRoleMenu.ID).provide();

        event.getInteraction().getChannel().block().createMessage(MessageCreateSpec.builder()
                .content(
                        "\"¡Bienvenidos y bienvenidas al servidor de estudiantes de **IT Academy**! Te invitamos a escoger en el desplegable que hay al final de este mensaje los lenguajes de programación que estás estudiando, dominas o que te interesan. Esto nos ayudará a desbloquear los canales específicos de esos lenguajes. ¡Esperamos que disfrutes de tu estancia en nuestra comunidad!\"")
                .addComponent(ActionRow.of(MENU)).build())
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
