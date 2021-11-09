package dev.sbs.api.data.model.reforge_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.rarities.RarityModel;
import dev.sbs.api.data.model.reforges.ReforgeModel;

import java.util.Map;

public interface ReforgeStatModel extends Model {

    ReforgeModel getReforge();

    RarityModel getRarity();

    Map<String, Object> getEffects();

}
