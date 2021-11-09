package dev.sbs.api.data.model.dungeon_levels;

import dev.sbs.api.data.model.Model;

public interface DungeonLevelModel extends Model {

    int getLevel();

    double getTotalExpRequired();

}
