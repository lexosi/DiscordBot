package discordbot;

import discordbot.manager.CommandManager;
import discordbot.manager.DiscordManager;

import java.lang.reflect.Constructor;
import java.util.Collection;

import discordbot.handler.DiscordCommand;
import discordbot.handler.events.CommandReceiveEvent;
import discordbot.handler.events.MessageEvent;
import discordbot.manager.EventManager;
import discordbot.utilities.ReflectionUtilities;

public class App {

    public static void main(String[] args) {
        App.register();

        DiscordManager.INSTANCE.login();
    }

    private static void register() {
        EventManager.INSTANCE.register(new MessageEvent());
        EventManager.INSTANCE.register(new CommandReceiveEvent());

        final Collection<Class<?>> COMMAND_CLASSES = ReflectionUtilities
                .readFilesFromFolder("discordbot.handler.commands");

        for (Class<?> commandClass : COMMAND_CLASSES) {
            try {
                final Constructor<?> constructor = commandClass.getDeclaredConstructor();
                if (constructor == null) {
                    continue;
                }
                constructor.setAccessible(true);

                final Object command = constructor.newInstance();
                if (command instanceof DiscordCommand discordComand) {
                    CommandManager.INSTANCE.register(discordComand);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
