package dev.sbs.api.data.model.pet_ability_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.pet_abilities.PetAbilityModel;
import dev.sbs.api.data.model.stats.StatModel;

import java.util.List;

public interface PetAbilityStatModel extends Model {

    PetAbilityModel getAbility();

    List<Integer> getRarities();

    StatModel getStat();

    double getBaseValue();

    double getLevelBonus();

    boolean isRoundingNeeded();

}
