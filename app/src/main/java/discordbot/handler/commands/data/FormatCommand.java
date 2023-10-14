package discordbot.handler.commands.data;

import java.util.List;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discordbot.handler.DiscordCommand;
import java.util.ArrayList;

public class FormatCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {

        final String LANG = interaction.getOption("language").get().getValue().get().asString();
        final String TEXT = interaction.getOption("text").get().getValue().get().asString();

        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content("```" + LANG + "\n" + TEXT + "```")
                .build()).subscribe();
    }

    @Override
    public List<ApplicationCommandOptionData> getCommandOptions() {
        return new ArrayList<>() {
            {
                this.add(ApplicationCommandOptionData.builder()
                        .name("language")
                        .description("You need to write the language that you want to use.")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build());

                this.add(ApplicationCommandOptionData.builder()
                        .name("text")
                        .description("You need to write the text that you want to format.")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build());
            }
        };
    }

    @Override
    public String getCommandDescription() {
        return "Parse code section and format it.";
    }
}