package dev.sbs.api.data.model.dungeon_floors;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.dungeon_bosses.DungeonBossModel;
import dev.sbs.api.data.model.dungeon_floor_sizes.DungeonFloorSizeModel;
import dev.sbs.api.data.model.dungeons.DungeonModel;

public interface DungeonFloorModel extends Model {

    DungeonModel getDungeon();

    int getFloor();

    DungeonFloorSizeModel getFloorSize();

    DungeonBossModel getFloorBoss();

}
