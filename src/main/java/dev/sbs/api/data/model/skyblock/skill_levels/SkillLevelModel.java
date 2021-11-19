package dev.sbs.api.data.model.skyblock.skill_levels;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

import java.util.List;

public interface SkillLevelModel extends EffectsModel {

    SkillModel getSkill();

    int getLevel();

    double getTotalExpRequired();

    List<String> getUnlocks();

}
