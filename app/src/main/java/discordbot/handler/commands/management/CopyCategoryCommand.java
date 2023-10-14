package discordbot.handler.commands.management;

import java.util.ArrayList;
import java.util.List;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.ExtendedPermissionOverwrite;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.PermissionOverwrite.Type;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.channel.CategorizableChannel;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.spec.NewsChannelCreateSpec;
import discord4j.core.spec.TextChannelCreateSpec;
import discord4j.core.spec.VoiceChannelCreateSpec;
import discord4j.core.object.entity.Guild;

import discordbot.handler.DiscordCommand;

public class CopyCategoryCommand implements DiscordCommand {

    @Override
    public List<ApplicationCommandOptionData> getCommandOptions() {
        return new ArrayList<>() {
            {
                this.add(ApplicationCommandOptionData.builder()
                        .name("category")
                        .description("You need to write the category name that you want to copy.")
                        .type(ApplicationCommandOption.Type.CHANNEL.getValue())
                        .channelTypes(List.of(Channel.Type.GUILD_CATEGORY.getValue()))
                        .required(true)
                        .build());

                this.add(ApplicationCommandOptionData.builder()
                        .name("name")
                        .description("You need to write the new name that you want that have the copy.")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build());
            }
        };
    }

    @Override
    public void execute(ApplicationCommandInteractionEvent event, ApplicationCommandInteraction interaction) {
        final Channel categoryChannel = interaction.getOption("category").get().getValue().get().asChannel().block();
        final String name = interaction.getOption("name").get().getValue().get().asString();

        if (categoryChannel.getType() != Channel.Type.GUILD_CATEGORY) {
            event.reply("No has introducido una categoría correcta.")
                    .withEphemeral(true)
                    .block();
            return;
        }

        if (!(categoryChannel instanceof Category category)) {
            event.reply("El tipo introducido no consta como una categoría.")
                    .withEphemeral(true)
                    .block();
            return;
        }

        final Guild guild = category.getGuild().block();
        final Category newCategory = guild.createCategory(name).block();
        /* Copy permissions from one category to another */
        for (ExtendedPermissionOverwrite permissionOverwrite : category.getPermissionOverwrites()) {
            final Type type = permissionOverwrite.getType();

            if (type == Type.MEMBER) {
                newCategory.addMemberOverwrite(
                        permissionOverwrite.getTargetId(),
                        permissionOverwrite,
                        "Copy channel to new category.").block();
                continue;
            }

            if (type == Type.ROLE) {
                newCategory.addRoleOverwrite(
                        permissionOverwrite.getTargetId(),
                        permissionOverwrite,
                        "Copy channel to new category.").block();
                continue;
            }
        }

        final List<CategorizableChannel> channelList = category.getChannels().collectList().block();
        for (CategorizableChannel channel : channelList) {
            final Channel.Type channelType = channel.getType();
            final List<PermissionOverwrite> permissionOverwrites = channel.getPermissionOverwrites().stream()
                    .map(permission -> (PermissionOverwrite) permission)
                    .toList();

            if (channelType == Channel.Type.GUILD_TEXT) {
                guild.createTextChannel(TextChannelCreateSpec.builder()
                        .name(channel.getName().replace(
                                category.getName().toLowerCase(),
                                newCategory.getName().toLowerCase()))
                        .parentId(newCategory.getId())
                        .addAllPermissionOverwrites(permissionOverwrites)
                        .build()).block();
                continue;
            }

            if (channelType == Channel.Type.GUILD_VOICE) {
                guild.createVoiceChannel(VoiceChannelCreateSpec.builder()
                        .name(channel.getName().replace(
                                category.getName(),
                                newCategory.getName()))
                        .parentId(newCategory.getId())
                        .addAllPermissionOverwrites(permissionOverwrites)
                        .build()).block();
                continue;
            }

            if (channelType == Channel.Type.GUILD_NEWS) {
                guild.createNewsChannel(NewsChannelCreateSpec.builder()
                        .name(channel.getName().replace(
                                category.getName().toLowerCase(),
                                newCategory.getName().toLowerCase()))
                        .parentId(newCategory.getId())
                        .addAllPermissionOverwrites(permissionOverwrites)
                        .build()).block();
                continue;
            }
        }

        event.reply("Se ha copiado la categoría correctamente.")
                .withEphemeral(true)
                .block();
    }

    @Override
    public String getCommandDescription() {
        return "Copy category";
    }

}
