package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface SkillModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    int getMaxLevel();

    ItemModel getItem();

}
