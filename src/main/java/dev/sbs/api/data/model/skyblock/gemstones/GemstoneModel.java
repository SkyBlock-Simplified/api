package dev.sbs.api.data.model.skyblock.gemstones;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

public interface GemstoneModel extends Model {

    String getKey();

    String getName();

    StatModel getStat();

}
