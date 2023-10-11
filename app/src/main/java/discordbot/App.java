package discordbot;

import discordbot.manager.CommandManager;
import discordbot.manager.DiscordManager;
import discordbot.handler.commands.DiceCommand;
import discordbot.handler.commands.FormatCommand;
import discordbot.handler.commands.HelloCommand;
import discordbot.handler.commands.TrainingCommand;
import discordbot.handler.events.CommandReceiveEvent;
import discordbot.handler.events.MessageEvent;
import discordbot.manager.EventManager;

public class App {

    public static void main(String[] args) {
        App.register();
        
        DiscordManager.INSTANCE.login();
    }

    private static void register() {
        EventManager.INSTANCE.register(new MessageEvent());
        EventManager.INSTANCE.register(new CommandReceiveEvent());

        CommandManager.INSTANCE.register(new HelloCommand());
        CommandManager.INSTANCE.register(new FormatCommand());
        CommandManager.INSTANCE.register(new DiceCommand());
        CommandManager.INSTANCE.register(new TrainingCommand());
    }
}
