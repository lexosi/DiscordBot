package discordbot.manager;

import discordbot.handler.DiscordCommand;

public abstract class CommandManager extends AbstractManager<String, DiscordCommand> {
    public static final CommandManager INSTANCE = new CommandManager() {
    };

    public void register(DiscordCommand command) {
        this.register(command.getCommandName(), command);
    }
}