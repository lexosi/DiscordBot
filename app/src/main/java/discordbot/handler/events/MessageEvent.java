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
        final Member member = event.getMember().orElse(null);
        if (member == null || member.isBot()) {
            return;
        }

        final String content = event.getMessage().getContent();
        if (content.startsWith("!say ")) {
            final String result = content.substring(5);
            event.getMessage().getChannel().block().createMessage(result).block();
            event.getMessage().delete().block();
        }
    }
}