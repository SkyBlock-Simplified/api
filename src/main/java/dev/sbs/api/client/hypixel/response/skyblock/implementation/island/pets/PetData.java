package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.pet_data.pet_scores.PetScoreModel;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StreamUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PetData {

    @Getter private final ConcurrentList<Pet> pets;
    private final AutoPet autopet;

    public Optional<Pet> getActivePet() {
        return this.getPets().stream().filter(Pet::isActive).findFirst();
    }

    public int getAutopetLimit() {
        return this.autopet.getRulesLimit();
    }

    public ConcurrentList<AutoPet.Rule> getAutopetRules() {
        return this.autopet.getRules();
    }

    public int getPetScore() {
        return this.getPets()
            .sorted(Pet::getRarity)
            .stream()
            .filter(StreamUtil.distinctByKey(Pet::getName))
            .mapToInt(pet -> pet.getRarityOrdinal() + 1)
            .sum();
    }

    public int getPetScoreMagicFind() {
        return SimplifiedApi.getRepositoryOf(PetScoreModel.class)
            .matchAll(petScoreModel -> petScoreModel.getBreakpoint() <= this.getPetScore())
            .getFirst()
            .map(PetScoreModel::getId)
            .orElse(0L)
            .intValue();
    }

}
