package dev.sbs.api.data.model.discord.settings;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.setting_evals.SettingEvalModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeModel;

public interface SettingModel extends Model {

    String getKey();

    String getName();

    SettingTypeModel getType();

    SettingEvalModel getEval();

    String getValue();

}
