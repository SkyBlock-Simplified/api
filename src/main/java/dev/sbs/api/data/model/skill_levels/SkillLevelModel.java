package dev.sbs.api.data.model.skill_levels;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skills.SkillModel;

import java.util.List;

public interface SkillLevelModel extends Model {

    SkillModel getSkill();

    Integer getLevel();

    Double getTotalExpRequired();

    List<String> getUnlocks();

}
