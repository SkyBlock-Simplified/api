package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.Model;

import java.time.Instant;

public interface CommandConfigModel extends Model {

    String getName();

    boolean isDeveloperOnly();

    boolean isEnabled();

    boolean isGuildToggleable();

    String getStatus();

    Instant getSubmittedAt();

}
