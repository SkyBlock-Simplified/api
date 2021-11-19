package dev.sbs.api.data.model.skill_levels;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skills.SkillModel;

import java.util.List;

public interface SkillLevelModel extends EffectsModel {

    SkillModel getSkill();

    int getLevel();

    double getTotalExpRequired();

    List<String> getUnlocks();

}
