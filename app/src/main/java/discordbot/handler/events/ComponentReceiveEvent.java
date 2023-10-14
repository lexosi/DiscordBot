package discordbot.handler.events;

import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discordbot.handler.DiscordComponent;
import discordbot.handler.DiscordEvent;
import discordbot.manager.discord.ComponentManager;

public class ComponentReceiveEvent implements DiscordEvent<ComponentInteractionEvent> {

    @Override
    public Class<ComponentInteractionEvent> getEventClass() {
        return ComponentInteractionEvent.class;
    }

    @Override
    public void execute(ComponentInteractionEvent event) {
        try {
            final DiscordComponent<?> COMPONENT = ComponentManager.INSTANCE.getValue(event.getCustomId());
            if (COMPONENT == null) {
                event.reply("Component not found").withEphemeral(true).subscribe();
                return;
            }

            COMPONENT.execute(event);
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("An error occurred").withEphemeral(true).subscribe();
        }
    }

}
