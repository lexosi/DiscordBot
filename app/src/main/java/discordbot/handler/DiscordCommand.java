package discordbot.handler;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import java.util.ArrayList;
import java.util.List;

public interface DiscordCommand {

    default String getCommandName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("command", "");
    }

    default List<ApplicationCommandOptionData> getCommandOptions() {
        return new ArrayList<>();
    }

    void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction);

    String getCommandDescription();

    default ApplicationCommandRequest getCommandRequest() {
        return ApplicationCommandRequest.builder()
                .name(this.getCommandName())
                .description(this.getCommandDescription())
                .addAllOptions(this.getCommandOptions())
                .build();
    }

}