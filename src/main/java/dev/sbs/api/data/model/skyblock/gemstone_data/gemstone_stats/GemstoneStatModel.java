package dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_types.GemstoneTypeModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstones.GemstoneModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface GemstoneStatModel extends Model {

    GemstoneModel getGemstone();

    GemstoneTypeModel getType();

    RarityModel getRarity();

    Double getValue();

}