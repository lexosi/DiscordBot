package discordbot.handler;

import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discord4j.core.object.entity.Member;

public interface DiscordComponent<T> {

    default String getComponentId() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    void execute(ComponentInteractionEvent interactionEvent);

    default T provide() {
        return this.provide(null);
    }

    T provide(Member member);
}