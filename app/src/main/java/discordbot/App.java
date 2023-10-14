package discordbot;

import java.lang.reflect.Constructor;
import java.util.Collection;

import discordbot.utilities.ReflectionUtilities;
import discordbot.manager.DiscordManager;
import discordbot.manager.database.MysqlConnection;
import discordbot.handler.DiscordCommand;
import discordbot.handler.DiscordComponent;
import discordbot.handler.DiscordEvent;
import discordbot.manager.discord.*;

public class App {

    public static void main(String[] args) {
        App.register();

        MysqlConnection.INSTANCE.connectMySQL();
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

        final Collection<Class<?>> COMPONENT_CLASSES = ReflectionUtilities
                .readFilesFromFolder("discordbot.handler.components");

        for (Class<?> componentClass : COMPONENT_CLASSES) {
            try {
                final Constructor<?> constructor = componentClass.getDeclaredConstructor();
                if (constructor == null) {
                    continue;
                }
                constructor.setAccessible(true);

                final Object component = constructor.newInstance();
                if (component instanceof DiscordComponent discordComponent) {
                    ComponentManager.INSTANCE.register(discordComponent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
