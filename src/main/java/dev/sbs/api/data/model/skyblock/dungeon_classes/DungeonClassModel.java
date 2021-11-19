package dev.sbs.api.data.model.skyblock.dungeon_classes;

import dev.sbs.api.data.model.Model;

public interface DungeonClassModel extends Model {

    String getKey();

    String getName();

    double getWeightMultiplier();

}
