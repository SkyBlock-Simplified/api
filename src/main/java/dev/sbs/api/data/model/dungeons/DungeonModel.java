package dev.sbs.api.data.model.dungeons;

import dev.sbs.api.data.model.Model;

public interface DungeonModel extends Model {

    String getKey();

    String getName();

    Double getWeightMultiplier();

}
