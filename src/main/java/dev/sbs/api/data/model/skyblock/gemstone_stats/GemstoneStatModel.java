package dev.sbs.api.data.model.skyblock.gemstone_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.gemstone_types.GemstoneTypeModel;
import dev.sbs.api.data.model.skyblock.gemstones.GemstoneModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface GemstoneStatModel extends Model {

    GemstoneModel getGemstone();

    GemstoneTypeModel getType();

    RarityModel getRarity();

    Double getValue();

}
