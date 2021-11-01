package dev.sbs.api.data.model.skills;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.items.ItemModel;

public interface SkillModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    int getMaxLevel();

    ItemModel getItem();

    boolean isCosmetic();

    double getWeightExponent();

    double getWeightDivider();

}
