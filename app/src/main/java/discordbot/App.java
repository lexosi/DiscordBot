package discordbot;

import discordbot.manager.DiscordManager;

import discordbot.handler.events.MessageEvent;
import discordbot.manager.EventManager;

public class App {

    public static void main(String[] args) {
        App.register();
        
        DiscordManager.INSTANCE.login();
    }

    private static void register() {
        EventManager.INSTANCE.register(new MessageEvent());
    }
}
