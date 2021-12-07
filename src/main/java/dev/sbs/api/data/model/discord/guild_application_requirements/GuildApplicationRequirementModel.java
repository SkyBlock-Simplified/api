package dev.sbs.api.data.model.discord.guild_application_requirements;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementModel;
import dev.sbs.api.data.model.discord.guild_applications.GuildApplicationModel;
import dev.sbs.api.data.model.discord.setting_evals.SettingEvalModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeModel;

public interface GuildApplicationRequirementModel extends Model {

    GuildApplicationModel getApplication();

    ApplicationRequirementModel getRequirement();

    SettingEvalModel getEval();

    SettingTypeModel getType();

    String getValue();

    String getDescription();

}
