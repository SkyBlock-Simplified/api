package dev.sbs.api.data.model.discord.users;

import dev.sbs.api.data.model.Model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserModel extends Model {

    List<Long> getDiscordIds();

    List<UUID> getMojangUniqueIds();

    Map<Long, String> getNotes();

    List<Long> getGuildInteractionBlacklisted();

    boolean isDeveloperProtected();

    boolean isBotInteractionEnabled();

    Instant getSubmittedAt();

    Instant getUpdatedAt();

}
