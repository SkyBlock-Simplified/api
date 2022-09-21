package dev.sbs.api.data.model.discord.guild_data.guild_application_entries;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guild_data.guild_applications.GuildApplicationModel;

import java.time.Instant;

public interface GuildApplicationEntryModel extends Model {

    GuildApplicationModel getApplication();

    Long getSubmitterDiscordId();

    Instant getSubmittedAt();

}
