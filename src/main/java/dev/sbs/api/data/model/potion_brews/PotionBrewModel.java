package dev.sbs.api.data.model.potion_brews;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.npcs.NpcModel;
import dev.sbs.api.data.model.rarities.RarityModel;

public interface PotionBrewModel extends Model {

    String getKey();

    String getName();

    RarityModel getRarity();

    String getDescription();

    NpcModel getNpc();

    double getCoinCost();

    boolean isPercentage();

}
