package discordbot.handler.events;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discordbot.handler.DiscordCommand;
import discordbot.handler.DiscordEvent;

import discordbot.manager.CommandManager;

public class CommandReceiveEvent implements DiscordEvent<ApplicationCommandInteractionEvent> {

    @Override
    public Class<ApplicationCommandInteractionEvent> getEventClass() {
        return ApplicationCommandInteractionEvent.class;
    }

    @Override
    public void execute(ApplicationCommandInteractionEvent event) {
        final DiscordCommand COMMAND = CommandManager.INSTANCE.getValue(event.getCommandName());
        if (COMMAND == null) {
            event.reply("Command not found").subscribe();
            return;
        }
        COMMAND.execute(event);
    }
}