package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.command_categories.CommandCategoryModel;
import dev.sbs.api.data.model.discord.command_groups.CommandGroupModel;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

import java.time.Instant;
import java.util.UUID;

public interface CommandConfigModel extends Model {

    UUID getUniqueId();

    String getCommandPath();

    String getDescription();

    String getLongDescription();

    EmojiModel getEmoji();

    CommandCategoryModel getCategory();

    CommandGroupModel getGroup();

    boolean isDeveloperOnly();

    boolean isEnabled();

    boolean isInheritingPermissions();

    boolean isGuildToggleable();

    String getStatus();

    Instant getSubmittedAt();

}
