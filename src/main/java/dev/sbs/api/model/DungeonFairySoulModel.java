package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface DungeonFairySoulModel extends Model {

    String getRoom();

    String getDescription();

    String getWhere();

    boolean isWalkable();

}
