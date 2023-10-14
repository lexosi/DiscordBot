package discordbot.handler.data;

import discord4j.core.object.reaction.ReactionEmoji;

public class DiscordEmoji {

    private final boolean animated;
    private final String name;
    private final long id;

    public DiscordEmoji(boolean animated, String name, long id) {
        this.animated = animated;
        this.name = name;
        this.id = id;
    }

    public boolean isAnimated() {
        return this.animated;
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.id;
    }

    public ReactionEmoji getReactionEmoji() {
        return ReactionEmoji.of(this.id, this.name, this.animated);
    }
}