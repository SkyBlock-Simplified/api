package dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilityModel;

public interface BonusPetAbilityStatModel extends BuffEffectsModel<Object, Double> {

    PetAbilityModel getPetAbility();

    boolean isPercentage();

    default boolean notPercentage() {
        return !this.isPercentage();
    }

}
