package dev.sbs.api.data.model.skyblock.dungeon_bosses;

import dev.sbs.api.data.model.Model;

public interface DungeonBossModel extends Model {

    String getKey();

    String getName();

    String getDescription();

}