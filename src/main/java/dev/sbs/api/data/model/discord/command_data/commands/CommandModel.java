package dev.sbs.api.data.model.discord.command_data.commands;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.command_data.command_categories.CommandCategoryModel;
import dev.sbs.api.data.model.discord.command_data.command_groups.CommandGroupModel;
import dev.sbs.api.data.model.discord.command_data.command_parents.CommandParentModel;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildModel;

import java.util.UUID;

public interface CommandModel extends Model {

    UUID getUniqueId();

    CommandParentModel getParent();

    CommandGroupModel getGroup();

    String getName();

    GuildModel getGuild();

    EmojiModel getEmoji();

    CommandCategoryModel getCategory();

    String getDescription();

    String getLongDescription();

    Long getDefaultMemberPermissions();

    boolean isNsfw();

    boolean isPrivateChannelEnabled();

    boolean isDeveloperOnly();

    boolean isEnabled();

    String getStatus();

}
