package discordbot.handler.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discordbot.handler.DiscordCommand;

public class DiceCommand implements DiscordCommand {

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {

        final Member MEMBER = event.getInteraction().getMember().orElse(null);
        if (MEMBER == null) {
            throw new IllegalStateException("Member is null");
        }

        event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .content("The number is " + this.randomDiceNumber() + "!")
                .ephemeral(false)
                .build()).block();
    }

    private int randomDiceNumber() {
        return (int) (Math.floor(Math.random() * (6 - 1 + 1) + 1));

    }

    @Override
    public String getCommandDescription() {
        return "🎲";
    }
}