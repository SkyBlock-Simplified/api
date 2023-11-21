package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_items.PetItemModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_levels.PetLevelModel;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class EnhancedPet extends Pet implements Experience {

    private final @NotNull Optional<PetModel> typeModel;
    private final @NotNull ConcurrentList<Double> experienceTiers;

    EnhancedPet(@NotNull Pet pet) {
        // Re-initialize Fields
        this.identifier = pet.getIdentifier();
        this.uniqueId = pet.getIdentifier();
        this.type = pet.getType();
        this.experience = pet.getExperience();
        this.active = pet.isActive();
        this.baseRarity = pet.getBaseRarity();
        this.candyUsed = pet.getCandyUsed();
        this.heldItem = pet.getHeldItem();
        this.skin = pet.getSkin();

        this.typeModel = SimplifiedApi.getRepositoryOf(PetModel.class).findFirst(PetModel::getKey, this.getType());
        this.experienceTiers = SimplifiedApi.getRepositoryOf(PetLevelModel.class)
            .findAll(PetLevelModel::getRarityOrdinal, Math.min(this.getRarity().ordinal(), 4))
            .stream()
            .map(PetLevelModel::getValue)
            .collect(Concurrent.toList());
    }

    public @NotNull Optional<ItemModel> getHeldItemModel() {
        return this.getHeldItem().flatMap(heldItem -> SimplifiedApi.getRepositoryOf(ItemModel.class).findFirst(ItemModel::getItemId, heldItem));
    }

    public @NotNull Optional<PetItemModel> getHeldPetItemModel() {
        return this.getHeldItem().flatMap(itemModel -> SimplifiedApi.getRepositoryOf(PetItemModel.class).findFirst(PetItemModel::getItem, itemModel));
    }

    @Override
    public int getMaxLevel() {
        return this.getTypeModel().map(PetModel::getMaxLevel).orElse(100);
    }

    public @NotNull RarityModel getRarityModel() {
        return SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getOrdinal, this.getRarity().ordinal());
    }

    @Override
    public int getStartingLevel() {
        return 1;
    }

}
