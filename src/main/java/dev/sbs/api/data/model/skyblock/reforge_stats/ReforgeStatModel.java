package dev.sbs.api.data.model.skyblock.reforge_stats;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;

public interface ReforgeStatModel extends EffectsModel {

    ReforgeModel getReforge();

    RarityModel getRarity();

}
