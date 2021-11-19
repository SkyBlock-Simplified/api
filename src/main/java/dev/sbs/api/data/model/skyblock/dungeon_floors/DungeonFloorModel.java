package dev.sbs.api.data.model.skyblock.dungeon_floors;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.dungeon_bosses.DungeonBossModel;
import dev.sbs.api.data.model.skyblock.dungeon_floor_sizes.DungeonFloorSizeModel;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonModel;

public interface DungeonFloorModel extends Model {

    DungeonModel getDungeon();

    Integer getFloor();

    DungeonFloorSizeModel getFloorSize();

    DungeonBossModel getFloorBoss();

}
