package discordbot.handler.components;

import discord4j.core.event.domain.interaction.ComponentInteractionEvent;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Member;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.InteractionPresentModalSpec;

import discordbot.handler.DiscordComponent;
import discordbot.manager.discord.ComponentManager;

public class OpenAuthModalButton implements DiscordComponent<Button> {

    public static String ID = "openAuthModal";

    @Override
    public void execute(ComponentInteractionEvent interactionEvent) {
        final InteractionPresentModalSpec MODAL = (InteractionPresentModalSpec) ComponentManager.INSTANCE
                .getValue(AuthModal.ID).provide(interactionEvent.getInteraction().getMember().orElse(null));

        interactionEvent.presentModal(MODAL).subscribe();

        /*
         * interactionEvent.deferEdit(InteractionCallbackSpec.builder()
         * .ephemeral(true).build()).block();
         * 
         * interactionEvent.
         * editReply("Conteste a este formulario para poder ingresar al servidor.").
         * subscribe();
         */
    }

    @Override
    public Button provide(Member member) {
        return Button.secondary(this.getComponentId(),
                ReactionEmoji.of(844330370995585064L, "github", false),
                "Abrir autentificador");
    }

    @Override
    public String getComponentId() {
        return OpenAuthModalButton.ID;
    }
}