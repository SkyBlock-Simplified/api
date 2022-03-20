package dev.sbs.api.data.model.discord.guild_command_configs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;

import java.time.Instant;
import java.util.List;

public interface GuildCommandConfigModel extends Model {

    GuildModel getGuild();

    String getName();

    List<Long> getUsers();

    List<Long> getRoles();

    Instant getSubmittedAt();

}
