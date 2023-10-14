package discordbot.manager.discord;

import discordbot.handler.DiscordComponent;
import discordbot.manager.AbstractManager;

public abstract class ComponentManager extends AbstractManager<String, DiscordComponent<?>> {
    public static final ComponentManager INSTANCE = new ComponentManager() {
    };

    public void register(DiscordComponent<?> component) {
        this.register(component.getComponentId(), component);
    }
}
