package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets;

import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
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
        int petScore = 1;
        ConcurrentList<String> noDuplicatePets = Concurrent.newList();

        for (Pet pet : this.getPets()) {
            if (noDuplicatePets.contains(pet.getName()))
                continue;

            petScore += pet.getRarity().getOrdinal() + 1;
            noDuplicatePets.add(pet.getName());
        }

        return petScore;
    }

}
