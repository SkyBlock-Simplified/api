package dev.sbs.api.data.model.skyblock.melodys_songs;

import dev.sbs.api.data.model.Model;

public interface MelodySongModel extends Model {

    String getKey();

    String getName();

    Integer getReward();

}
