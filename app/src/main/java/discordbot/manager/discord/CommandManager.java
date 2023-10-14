package discordbot.manager.discord;

import discordbot.handler.DiscordCommand;
import discordbot.manager.AbstractManager;

public abstract class CommandManager extends AbstractManager<String, DiscordCommand> {
    public static final CommandManager INSTANCE = new CommandManager() {
    };

    public void register(DiscordCommand command) {
        this.register(command.getCommandName(), command);
    }
}