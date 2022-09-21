package dev.sbs.api.data.model.skyblock.collection_data.collections;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

public interface CollectionModel extends Model {

    SkillModel getSkill();

}
