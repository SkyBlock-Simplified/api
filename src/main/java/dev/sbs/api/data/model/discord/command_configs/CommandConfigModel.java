package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

import java.time.Instant;

public interface CommandConfigModel extends Model {

    String getName();

    String getDescription();

    String getLongDescription();

    EmojiModel getEmoji();

    boolean isDeveloperOnly();

    boolean isEnabled();

    boolean isGuildToggleable();

    String getStatus();

    Instant getSubmittedAt();

}
