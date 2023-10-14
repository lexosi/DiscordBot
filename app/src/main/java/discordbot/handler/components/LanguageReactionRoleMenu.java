package discordbot.handler.components;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.component.SelectMenu.Option;
import discord4j.core.object.entity.Member;

import discordbot.handler.DiscordComponent;
import discordbot.handler.data.DiscordEmoji;
import discordbot.handler.data.DiscordRole;
import discordbot.manager.database.MysqlConnection;

public class LanguageReactionRoleMenu implements DiscordComponent<SelectMenu> {

    public static final String ID = "languageRoleMenu";

    private static final Map<String, DiscordRole> ROLES = new HashMap<>() {
        {
            this.put("java", new DiscordRole("java", "Java", 1162676733678469252L,
                    new DiscordEmoji(false, "java", 1162678817119273054L)));

            this.put("react", new DiscordRole("react", "React", 1162676789655650324L,
                    new DiscordEmoji(false, "reactjs", 1162678807401082921L)));

            this.put("angular", new DiscordRole("angular", "Angular", 1162676895872192582L,
                    new DiscordEmoji(false, "angular", 1162678810135761008L)));

            this.put("nodejs", new DiscordRole("nodejs", "NodeJS", 1162676764569518112L,
                    new DiscordEmoji(false, "nodejs", 1162678814254579743L)));

            this.put("vue", new DiscordRole("vue", "Vue", 1162676924427018300L,
                    new DiscordEmoji(false, "vuejs", 1162678808927797338L)));

            this.put("php", new DiscordRole("php", "PHP", 1162676998926245939L,
                    new DiscordEmoji(false, "php", 1162680435671834644L)));

            this.put("py", new DiscordRole("py", "Python", 1162676816645992468L,
                    new DiscordEmoji(false, "python", 1162678804779638924L)));

            this.put("bigdata", new DiscordRole("bigdata", "Big Data", 1162677117365006466L,
                    new DiscordEmoji(false, "bigdata", 1162681196229181480L)));

            this.put("mysql", new DiscordRole("mysql", "MySQL", 1162681555722969118L,
                    new DiscordEmoji(false, "mysql", 1162681699914764376L)));

            this.put("mongodb", new DiscordRole("mongodb", "MongoDB", 1162681598655856680L,
                    new DiscordEmoji(false, "mongodb", 1162681894643716136L)));
        }
    };

    @Override
    public void execute(ComponentInteractionEvent interactionEvent) {
        final Member member = interactionEvent.getInteraction().getMember().orElse(null);

        if (member == null) {
            interactionEvent.reply("Error al otorgar los roles, el usuario no existe.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }

        if (!(interactionEvent instanceof SelectMenuInteractionEvent interactionMenuEvent)) {
            interactionEvent.reply("Error interno al otorgar los roles.")
                    .withEphemeral(true)
                    .subscribe();
            return;
        }

        interactionEvent.deferReply()
                .withEphemeral(true)
                .block();
        CompletableFuture.runAsync(() -> {
            // Remove other roles
            for (DiscordRole role : LanguageReactionRoleMenu.ROLES.values()) {
                try {
                    member.removeRole(Snowflake.of(role.getRoleId())).block();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (String value : interactionMenuEvent.getValues()) {
                final DiscordRole role = LanguageReactionRoleMenu.ROLES.get(value);
                if (role == null) {
                    System.out.println("Role not found: " + value);
                    continue;
                }

                try {
                    member.addRole(Snowflake.of(role.getRoleId())).subscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            interactionEvent.editReply("Roles otorgados con éxito.").subscribe();
        }, MysqlConnection.EXECUTOR);
    }

    /*
     * Java
     * React
     * Angular
     * NodeJS
     * Vue
     * PHP
     * Python
     * Big Data
     */
    @Override
    public SelectMenu provide(Member member) {

        final Option[] options = ROLES.entrySet().stream().map(entry -> {
            final DiscordRole role = entry.getValue();

            final Option option = Option.of(role.getName(), entry.getKey());
            return option.withEmoji(role.getEmoji().getReactionEmoji());
        }).toArray(Option[]::new);

        final SelectMenu selectMenu = SelectMenu.of(this.getComponentId(), options)
                .withPlaceholder("Selecciona cómo mínimo un lenguaje.")
                .withMinValues(1);
        return selectMenu.withMaxValues(selectMenu.getOptions().size());
    }

    @Override
    public String getComponentId() {
        return LanguageReactionRoleMenu.ID;
    }
}