package discordbot.manager;

import discord4j.core.event.domain.Event;
import discord4j.rest.service.ApplicationService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.DiscordClient;
import discordbot.Constants;

import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import discordbot.handler.DiscordCommand;
import discordbot.handler.DiscordEvent;

public abstract class DiscordManager {
    public static final DiscordManager INSTANCE = new DiscordManager() {
    };

    private GatewayDiscordClient discordClient;

    public void login() {
        final DiscordClient CLIENT = DiscordClient.create(Constants.TOKEN);
        CLIENT.withGateway(gateway -> {
            this.registerEvents(gateway);
            this.registerCommands(gateway);
            return Mono.empty();
        }).block();
    }

    public GatewayDiscordClient getClient() {
        return this.discordClient;
    }

    private void registerEvents(GatewayDiscordClient client) {
        if (client == null) {
            System.out.println("Discord client is null, cannot register events.");
            return;
        }

        discordClient = client;
        final Set<Class<? extends Event>> EVENT_LIST = EventManager.INSTANCE.getValues().values()
                .stream()
                .map(event -> event.getEventClass())
                .collect(Collectors.toSet());
                
        for (Class<? extends Event> eventClass : EVENT_LIST) {
            client.on(eventClass).subscribe(event -> {
                final List<DiscordEvent<?>> EVENTS = EventManager.INSTANCE
                    .getFilteredValues(value -> value.getEventClass().isAssignableFrom(eventClass));
                
                for (DiscordEvent<?> eventHandler : EVENTS) {
                    eventHandler.handle(event);
                }
            });
        }

        System.out.println("Registered " + EVENT_LIST.size() + " events.");
    }

    private void registerCommands(GatewayDiscordClient client) {
        final ApplicationService SERVICE = client.getRestClient().getApplicationService();
        final Collection<DiscordCommand> COMMANDS = CommandManager.INSTANCE.getValues().values();

        final long CLIENT_ID = client.getRestClient().getApplicationId().block();
        for (DiscordCommand command : COMMANDS) {
            SERVICE.createGlobalApplicationCommand(CLIENT_ID, command.getCommandRequest()).subscribe();
        }

        System.out.println("Registered " + COMMANDS.size() + " commands.");
    }
}
