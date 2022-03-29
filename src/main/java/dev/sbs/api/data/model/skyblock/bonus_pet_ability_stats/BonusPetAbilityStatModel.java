package dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.discord.optimizer_mob_types.OptimizerMobTypeModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilityModel;

public interface BonusPetAbilityStatModel extends BuffEffectsModel<Object, Double> {

    PetAbilityModel getPetAbility();

    boolean isPercentage();

    ItemModel getRequiredItem();

    OptimizerMobTypeModel getRequiredMobType();

    default boolean hasRequiredItem() {
        return this.getRequiredItem() != null;
    }

    default boolean hasRequiredMobType() {
        return this.getRequiredMobType() != null;
    }

    default boolean notPercentage() {
        return !this.isPercentage();
    }

    default boolean noRequiredItem() {
        return !this.hasRequiredItem();
    }

    default boolean noRequiredMobType() {
        return !this.hasRequiredMobType();
    }

}
