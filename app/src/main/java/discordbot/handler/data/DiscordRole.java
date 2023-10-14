package discordbot.handler.data;

import discord4j.core.object.entity.Role;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;

public class DiscordRole {
    private String name;

    private final String id;
    private final long roleId;
    private final DiscordEmoji emoji;

    public DiscordRole(String id, String name, long roleId, DiscordEmoji emoji) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
        this.emoji = emoji;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public DiscordEmoji getEmoji() {
        return this.emoji;
    }

    public Role getRole(Guild guild) {
        return guild.getRoleById(Snowflake.of(this.roleId)).block();
    }
}