package dev.sbs.api.data.model.reforge_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.rarities.RarityModel;

import java.util.Map;

public interface ReforgeStatModel extends Model {

    String getKey();

    RarityModel getRarity();

    Map<String, Object> getEffects();

}
