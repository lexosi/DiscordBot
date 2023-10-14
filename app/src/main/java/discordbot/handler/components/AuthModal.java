package discordbot.handler.components;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.TextInput;
import discord4j.core.spec.InteractionPresentModalSpec;
import discord4j.discordjson.json.ComponentData;
import discord4j.discordjson.possible.Possible;
import discordbot.handler.DiscordComponent;
import discord4j.core.object.entity.Member;
import java.util.concurrent.CompletableFuture;

import discordbot.manager.database.MysqlConnection;

public class AuthModal implements DiscordComponent<InteractionPresentModalSpec> {

    public static final String ID = "authModal";

    private static final String AUTH_TITLE = "Autentificación con Github";

    @Override
    public void execute(ComponentInteractionEvent interactionEvent) {
        final Member member = interactionEvent.getInteraction().getMember().orElse(null);
        if (member == null) {
            interactionEvent.reply("Error al autentificar, el miembro de discord no existe.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }

        if (!(interactionEvent instanceof ModalSubmitInteractionEvent submitInteraction)) {
            System.out.println("Modal submit interaction is not ModalSubmitInteractionEvent");
            interactionEvent.reply("Error al autentificar.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }

        final Possible<List<ComponentData>> firstRow = submitInteraction.getComponents().get(0).getData().components();
        if (firstRow.isAbsent()) {
            interactionEvent.reply("Error al autentificar, no se han encontrado los datos.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }

        final Possible<String> possibleGithubUser = firstRow.get().get(0).value();
        if (possibleGithubUser.isAbsent()) {
            interactionEvent.reply("Error al autentificar, no se ha encontrado tu nombre.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }
        final String githubUser = possibleGithubUser.get();

        CompletableFuture.supplyAsync(() -> {
            try {

                final URL url = new URL("https://github.com/" + githubUser);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();
                final int response = connection.getResponseCode();
                if (response == 404) {
                    interactionEvent.reply("Error al autentificar, el usuario no existe.")
                            .withEphemeral(true)
                            .subscribe();
                    return false;
                }
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                interactionEvent.reply("Error al autentificar, el usuario no existe.")
                        .withEphemeral(true)
                        .subscribe();
                return false;
            }
        }, MysqlConnection.EXECUTOR).thenAcceptAsync(connected -> {
            if (!connected) {
                return;
            }

            /* Add member to the db */
            try {
                final long memberId = member.getId().asLong();

                /* Ayudarte a que lo veas mas claro, eso es un placeholder */
                // String.format("STATEMENT CON PLACEHOLDER %s", REEMPLAZOS);
                // String.format("%s %s.", "Hola", "Mundo");
                // String.format("Esto se reemplaza en orden: 1. %s 2. %s", "Hola", "Mundo")
                /* La primera %s corresponde con el primer elemento q pones para reemplazar */

                // Check if user exists
                if (MysqlConnection.INSTANCE.queryStatement(String.format(
                        "SELECT * FROM users WHERE id = %s", memberId)).next()) {
                    interactionEvent.reply("Ya estás registrado en el sistema.")
                            .withEphemeral(true)
                            .subscribe();
                    return;
                }

                // Check if the githubUser is duplicated
                if (MysqlConnection.INSTANCE.queryStatement(String.format(
                        "SELECT * FROM users WHERE github_user = '%s'", githubUser)).next()) {
                    interactionEvent.reply("El usuario de github ya está registrado.")
                            .withEphemeral(true)
                            .subscribe();
                    return;
                }

                // Add user to the db
                MysqlConnection.INSTANCE.executeStatement(String.format(
                        "INSERT INTO users(id, github_user) VALUES (%s, '%s')",
                        memberId, githubUser));

            } catch (Exception e) {
                e.printStackTrace();
                interactionEvent.reply("Error al autentificar con la base de datos.")
                        .withEphemeral(true)
                        .subscribe();
                return;
            }

            /* Add role verificado */
            member.addRole(Snowflake.of("1162315271244107796"), "Authenticated with Github").block();
            interactionEvent.reply("Has sido logeado con éxito con el usuario de github: " + githubUser)
                    .withEphemeral(true)
                    .subscribe();
        }, MysqlConnection.EXECUTOR);
    }

    public InteractionPresentModalSpec provide(Member member) {
        return InteractionPresentModalSpec.builder()
                .title(AuthModal.AUTH_TITLE)
                .customId(this.getComponentId())
                .addComponent(
                        ActionRow.of(
                                TextInput.small("githubUser", "Usuario de Github", 1, 39)
                                        .placeholder("user")
                                        .required(true)))
                .build();
    }

    @Override
    public String getComponentId() {
        return AuthModal.ID;
    }
}
