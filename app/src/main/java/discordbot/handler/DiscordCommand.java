package discordbot.handler;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;

public interface DiscordCommand {

    default String getCommandName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("command", "");
    }

    default ApplicationCommandRequest getCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getCommandName())
                .build();
    }

    void execute(ApplicationCommandInteractionEvent discord);
}