package discordbot;

import discordbot.manager.CommandManager;
import discordbot.manager.DiscordManager;

import java.lang.reflect.Constructor;
import java.util.Collection;

import discordbot.handler.DiscordCommand;
import discordbot.handler.DiscordEvent;
import discordbot.manager.EventManager;
import discordbot.utilities.ReflectionUtilities;

public class App {

    public static void main(String[] args) {
        App.register();

        DiscordManager.INSTANCE.login();
    }

    private static void register() {

        final Collection<Class<?>> EVENT_CLASSES = ReflectionUtilities
                .readFilesFromFolder("discordbot.handler.events");

        for (Class<?> eventClass : EVENT_CLASSES) {
            try {
                final Constructor<?> constructor = eventClass.getDeclaredConstructor();
                if (constructor == null) {
                    continue;
                }
                constructor.setAccessible(true);

                final Object event = constructor.newInstance();
                if (event instanceof DiscordEvent discordEvent) {
                    EventManager.INSTANCE.register(discordEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
