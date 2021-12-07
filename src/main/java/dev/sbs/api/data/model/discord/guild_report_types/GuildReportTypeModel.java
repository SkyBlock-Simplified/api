package dev.sbs.api.data.model.discord.guild_report_types;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;

public interface GuildReportTypeModel extends Model {

    GuildModel getGuild();

    String getKey();

    String getName();

    String getDescription();

}
