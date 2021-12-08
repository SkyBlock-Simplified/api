package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface UserModel extends Model {

    List<Long> getDiscordIds();

    List<String> getMojangUniqueIds();

    Map<Long, String> getNotes();

    Map<Long, List<String>> getGuildCommandsBlacklisted();

    Map<Long, List<String>> getGuildReputationBlacklisted();

    Map<Long, List<String>> getGuildTicketsBlacklisted();

    boolean isDeveloperProtected();

    boolean isDeveloperReportsEnabled();

    boolean isDeveloperReputationEnabled();

    boolean isDeveloperCommandsEnabled();

    List<String> getDeveloperCommandsBlacklisted();

    Instant getSubmittedAt();

    Instant getUpdatedAt();

}
