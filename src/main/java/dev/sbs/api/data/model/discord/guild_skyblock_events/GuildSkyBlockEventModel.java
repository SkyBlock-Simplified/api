package dev.sbs.api.data.model.discord.guild_skyblock_events;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventModel;

import java.util.List;

public interface GuildSkyBlockEventModel extends Model {

    GuildModel getGuild();

    SkyBlockEventModel getEvent();

    boolean isEnabled();

    List<Long> getMentionRoles();

    String getWebhookUrl();

}
