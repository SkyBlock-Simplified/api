package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet;

import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StreamUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PetData {

    private final static @NotNull ConcurrentList<Integer> magicFindPetScore = Concurrent.newList(10, 25, 50, 75, 100, 130, 175, 225, 275, 325, 375, 450, 500);
    private final @NotNull ConcurrentList<Pet> pets = Concurrent.newList();
    private final AutoPet autopet = new AutoPet();

    public @NotNull Optional<Pet> getActivePet() {
        return this.getPets()
            .stream()
            .filter(Pet::isActive)
            .findFirst();
    }

    public int getPetScore() {
        return this.getPets()
            .sorted(Pet::getRarity)
            .stream()
            .filter(StreamUtil.distinctByKey(Pet::getType))
            .mapToInt(Pet::getScore)
            .sum();
    }

    public int getPetScoreMagicFind() {
        return magicFindPetScore.matchAll(breakpoint -> breakpoint <= this.getPetScore())
            .getLast()
            .orElse(0);
    }

}
