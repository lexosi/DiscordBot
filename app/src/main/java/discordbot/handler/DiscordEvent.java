package discordbot.handler;

import discord4j.core.event.domain.Event;

public interface DiscordEvent<E extends Event> {

    default String getEventName() {
        return this.getClass().getSimpleName() + "_" + this.getEventClass().getSimpleName();
    }

    Class<E> getEventClass();

    void execute(E event);

    default void handle(Event event) {
        this.execute(this.getEventClass().cast(event));
    }
}