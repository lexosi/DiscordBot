package discordbot.handler.commands.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discordbot.manager.database.MysqlConnection;
import discordbot.handler.DiscordCommand;

public class GithubCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {

        // es un @user por lo que uso el asUser no el asString
        final User USER = interaction.getOption("user").get().getValue().get().asUser().block();

        CompletableFuture.supplyAsync(() -> {
            final long userId = USER.getId().asLong();

            try {
                /* Check if exists */

                // Obtenemos la query y el statement (Similar a un iterator)
                final ResultSet SET = MysqlConnection.INSTANCE.queryStatement(
                        String.format("SELECT github_user FROM users WHERE id = %s", userId));

                // Obtenemos el siguiente elemento y vemos si existe, si no existe:
                /*
                 * El error que obteniamos era "Before start", era porque estabamos
                 * intentando obtener el github_user sin haber movido al siguiente (primer)
                 * usuario, por eso es necesario ejecutar al menos una vez el next() antes
                 * al fin y al cabo es como un iterator, tenemos el elemento actual y con
                 * el next() pasamos al siguiente
                 */

                if (!SET.next()) {
                    event.reply("El usuario seleccionado no tiene un github asignado.")
                            .withEphemeral(true)
                            .block();
                    return null;
                }

                /* Get github user */
                return SET.getString("github_user");

            } catch (Exception e) {
                e.printStackTrace();
                event.reply("Hubo un error al recuperar el github del usuario seleccionado.")
                        .withEphemeral(true)
                        .block();
                return null;
            }

        }, MysqlConnection.EXECUTOR).thenAcceptAsync(githubUser -> {
            if (githubUser == null) {
                return;
            }

            event.reply(String.format(
                    "Github del usuario [" + USER.getUsername() + "](https://github.com/%s)",
                    githubUser)).block();
        });
    }

    @Override
    public List<ApplicationCommandOptionData> getCommandOptions() {
        return new ArrayList<>() {
            {
                this.add(ApplicationCommandOptionData.builder()
                        .name("user")
                        .description("Introduzca el usuario del que quieres saber su github.")
                        .type(ApplicationCommandOption.Type.USER.getValue())
                        .required(true)
                        .build());
            }
        };
    }

    @Override
    public String getCommandDescription() {
        return "Know Github profile of the user you select";
    }

}
