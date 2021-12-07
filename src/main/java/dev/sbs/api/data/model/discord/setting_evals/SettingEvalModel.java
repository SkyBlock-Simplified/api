package dev.sbs.api.data.model.discord.setting_evals;

import dev.sbs.api.data.model.Model;

public interface SettingEvalModel extends Model {

    String getKey();

    String getName();

    String getDescription();

}
