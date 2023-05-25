package dev.sbs.api.data.model.discord.guild_data.guild_application_requirements;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementModel;
import dev.sbs.api.data.model.discord.guild_data.guild_applications.GuildApplicationModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeModel;

public interface GuildApplicationRequirementModel extends Model {

    GuildModel getGuild();

    GuildApplicationModel getApplication();

    ApplicationRequirementModel getRequirement();

    SettingTypeModel getType();

    String getValue();

    String getDescription();

}
