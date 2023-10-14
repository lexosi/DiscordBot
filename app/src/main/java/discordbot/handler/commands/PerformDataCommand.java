package discordbot.handler.commands;

import discordbot.manager.database.MysqlConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discordbot.handler.DiscordCommand;

public class PerformDataCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        final String statement = interaction.getOption("data").get().getValue().get().asString();
        final String execute = interaction.getOption("execute").get().getValue().get().asString();

        try {

            String stringResult = "undefined";
            if (execute.equalsIgnoreCase("query")) {
                final ResultSet resultSet = MysqlConnection.INSTANCE.queryStatement(statement);
                if (resultSet != null) {
                    stringResult = resultSet.
                }
            } else {
                final boolean result = MysqlConnection.INSTANCE.executeStatement(statement);
                stringResult = result + "";
            }

            event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content("Ejecutado con el resultado: " + stringResult)
                    .build()).block();
        } catch (Exception e) {
            event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content("Error al ejecutar el comando: " + e.getMessage())
                    .build()).block();
        }
    }

    @Override
    public List<ApplicationCommandOptionData> getCommandOptions() {
        return new ArrayList<>() {
            {
                this.add(ApplicationCommandOptionData.builder()
                        .name("execute")
                        .description("'query' o 'execute'")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build());

                this.add(ApplicationCommandOptionData.builder()
                        .name("data")
                        .description("Statement.")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build());
            }
        };
    }

    @Override
    public String getCommandDescription() {
        return "TEMP: Ejecuta un comando predefinido de base de datos.";
    }
}
