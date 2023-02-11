package dev.sbs.api.data.model.discord.command_data.command_configs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.command_data.command_categories.CommandCategoryModel;
import dev.sbs.api.data.model.discord.command_data.command_groups.CommandGroupModel;
import dev.sbs.api.data.model.discord.command_data.command_parents.CommandParentModel;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

import java.time.Instant;
import java.util.UUID;

public interface CommandConfigModel extends Model {

    UUID getUniqueId();

    String getName();

    CommandGroupModel getGroup();

    CommandParentModel getParent();

    String getDescription();

    String getLongDescription();

    EmojiModel getEmoji();

    CommandCategoryModel getCategory();

    boolean isDeveloperOnly();

    boolean isEnabled();

    boolean isInheritingPermissions();

    String getStatus();

    Instant getSubmittedAt();

}
