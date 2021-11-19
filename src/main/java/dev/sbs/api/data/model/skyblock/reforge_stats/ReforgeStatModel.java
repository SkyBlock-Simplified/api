package dev.sbs.api.data.model.skyblock.reforge_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;

import java.util.Map;

public interface ReforgeStatModel extends Model {

    ReforgeModel getReforge();

    RarityModel getRarity();

    Map<String, Object> getEffects();

}
