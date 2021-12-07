package dev.sbs.api.data.model.discord.guilds;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;

public interface GuildModel extends Model {

    long getGuildId();

    String getName();

    boolean isReportsVisible();

    boolean isBotEnabled();

    boolean isEmojiServer();

    boolean isDeveloperReportsEnabled();

    boolean isDeveloperTicketsEnabled();

    boolean isDeveloperReputationEnabled();

    boolean isDeveloperBotEnabled();

    List<String> getDeveloperBlacklistedCommands();

    List<Long> getAdminRoles();

    List<Long> getManagerRoles();

    List<Long> getModRoles();

    List<Long> getHelperRoles();

    Instant getSubmittedAt();

}
