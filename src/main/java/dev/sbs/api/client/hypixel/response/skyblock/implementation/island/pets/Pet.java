package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_items.PetItemModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_levels.PetLevelModel;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.WordUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pet extends Experience {

    @SerializedName("uuid")
    @Getter private UUID uniqueId;
    @SerializedName("type")
    @Getter private String name;
    @SerializedName("exp")
    @Getter private double experience;
    @Getter private boolean active;
    @SerializedName("tier")
    private String rarityKey;
    @Getter private int candyUsed;
    private String heldItem;
    private String skin;
    private RarityModel baseRarity;
    private RarityModel rarity;

    public RarityModel getBaseRarity() {
        if (Objects.isNull(this.baseRarity))
            this.baseRarity = SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getKey, this.rarityKey);

        return this.baseRarity;
    }

    @Override
    public ConcurrentList<Double> getExperienceTiers() {
        return SimplifiedApi.getRepositoryOf(PetLevelModel.class)
            .findAll(PetLevelModel::getRarityOrdinal, Math.min(this.getRarityOrdinal(), 4))
            .stream()
            .map(PetLevelModel::getValue)
            .collect(Concurrent.toList());
    }

    public String getDefaultSkin() {
        return this.getPet().map(PetModel::getSkin).orElse("");
    }

    public Optional<ItemModel> getHeldItem() {
        return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirst(ItemModel::getItemId, this.heldItem);
    }

    public Optional<PetItemModel> getHeldPetItem() {
        return this.getHeldItem().flatMap(itemModel -> SimplifiedApi.getRepositoryOf(PetItemModel.class).findFirst(PetItemModel::getItem, itemModel));
    }

    @Override
    public int getMaxLevel() {
        return this.getPet().map(PetModel::getMaxLevel).orElse(100);
    }

    public Optional<PetModel> getPet() {
        return SimplifiedApi.getRepositoryOf(PetModel.class).findFirst(PetModel::getKey, this.getName());
    }

    public String getPrettyName() {
        return WordUtil.capitalizeFully(this.getName().replace("_", " "));
    }

    public RarityModel getRarity() {
        if (Objects.isNull(this.rarity)) {
            int rarityOrdinal = this.getBaseRarity().getOrdinal();

            if (this.isTierBoosted())
                rarityOrdinal++;

            if (this.isHeldItemBoosted())
                rarityOrdinal++;

            this.rarity = SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getOrdinal, rarityOrdinal);
        }

        return this.rarity;
    }

    public int getRarityOrdinal() {
        return this.getRarity().getOrdinal();
    }

    public Optional<String> getSkin() {
        return Optional.ofNullable(this.skin);
    }

    @Override
    public int getStartingLevel() {
        return 1;
    }

    public boolean isHeldItemBoosted() {
        return this.getHeldPetItem().map(PetItemModel::isRarityBoost).orElse(false);
    }

    public boolean isTierBoosted() {
        return this.getHeldItem().map(itemModel -> itemModel.getItemId().equals("PET_ITEM_TIER_BOOST")).orElse(false);
    }

}
