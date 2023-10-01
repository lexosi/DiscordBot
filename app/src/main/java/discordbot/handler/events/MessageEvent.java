package discordbot.handler.events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discordbot.handler.DiscordEvent;

public class MessageEvent implements DiscordEvent<MessageCreateEvent> {

    @Override
    public Class<MessageCreateEvent> getEventClass() {
        return MessageCreateEvent.class;
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final Member MEMBER = event.getMember().orElse(null);
        if (MEMBER == null || MEMBER.isBot()) {
            return;
        }

        if (event.getMessage().getContent().equalsIgnoreCase("!hello")) {
            event.getMessage().getChannel().block().createMessage("Hello World!").block();
        }
    }
}