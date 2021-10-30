package dev.sbs.api.data.model.collections;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skills.SkillModel;

public interface CollectionModel extends Model {

    SkillModel getSkill();

}
