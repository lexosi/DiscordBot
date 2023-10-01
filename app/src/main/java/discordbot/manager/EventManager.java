package discordbot.manager;

import discordbot.handler.DiscordEvent;

public abstract class EventManager extends AbstractManager<String, DiscordEvent<?>> {
    public static final EventManager INSTANCE = new EventManager() {
    };

    public void register(DiscordEvent<?> event) {
        this.register(event.getEventName(), event);
    } 
}