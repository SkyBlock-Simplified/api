package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;

public interface UserModel extends Model {

    List<Long> getDiscordIds();

    List<String> getMojangUniqueIds();

    String getNotes();

    List<Long> getGuildCommandsBlacklisted();

    List<Long> getGuildReputationBlacklisted();

    List<Long> getGuildTicketsBlacklisted();

    boolean isDeveloperProtected();

    boolean isDeveloperReportsEnabled();

    boolean isDeveloperReputationEnabled();

    boolean isDeveloperCommandsEnabled();

    List<String> getDeveloperCommandsBlacklisted();

    Instant getSubmittedAt();

    Instant getUpdatedAt();

}
