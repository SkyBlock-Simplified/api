package dev.sbs.api.data.model.skyblock.skill_levels;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

import java.util.List;

public interface SkillLevelModel extends BuffEffectsModel<Double> {

    SkillModel getSkill();

    Integer getLevel();

    Double getTotalExpRequired();

    List<String> getUnlocks();

}
