package dev.sbs.api.data.model.skyblock.pet_ability_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilityModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

import java.util.List;

public interface PetAbilityStatModel extends Model {

    PetAbilityModel getAbility();

    StatModel getStat();

    List<Integer> getRarities();

    Double getBaseValue();

    Double getLevelBonus();

    boolean isRoundingNeeded();

}
