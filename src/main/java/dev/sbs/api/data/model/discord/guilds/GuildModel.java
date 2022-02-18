package dev.sbs.api.data.model.discord.guilds;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;

public interface GuildModel extends Model {

    Long getGuildId();

    String getName();

    boolean isDeveloperBotEnabled();

    boolean isReportsPublic();

    boolean isEmojiServer();

    List<Long> getAdminRoles();

    List<Long> getManagerRoles();

    List<Long> getModRoles();

    List<Long> getHelperRoles();

    Instant getSubmittedAt();

}
