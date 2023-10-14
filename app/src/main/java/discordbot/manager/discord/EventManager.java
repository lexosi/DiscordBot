package discordbot.manager.discord;

import discordbot.handler.DiscordEvent;
import discordbot.manager.AbstractManager;

public abstract class EventManager extends AbstractManager<String, DiscordEvent<?>> {
    public static final EventManager INSTANCE = new EventManager() {
    };

    public void register(DiscordEvent<?> event) {
        this.register(event.getEventName(), event);
    }
}