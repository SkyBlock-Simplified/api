package dev.sbs.api.data.model.discord.command_configs;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;

public interface CommandConfigModel extends Model {

    String getName();

    boolean isBotOwnerOnly();

    List<Long> getUsers();

    List<Long> getRoles();

    boolean isEnabled();

    String getStatus();

    Instant getSubmittedAt();

}
