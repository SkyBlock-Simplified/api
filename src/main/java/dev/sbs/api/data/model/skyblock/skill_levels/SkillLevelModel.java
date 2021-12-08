package dev.sbs.api.data.model.skyblock.skill_levels;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

import java.util.List;
import java.util.Map;

public interface SkillLevelModel extends EffectsModel {

    SkillModel getSkill();

    Integer getLevel();

    Double getTotalExpRequired();

    List<String> getUnlocks();

    Map<String, ?> getBuffEffects();

}
