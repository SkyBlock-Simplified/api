package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.List;

public interface SkillLevelModel extends Model {

    SkillModel getSkill();

    int getLevel();

    double getTotalExpRequired();

    List<String> getUnlocks();

}
