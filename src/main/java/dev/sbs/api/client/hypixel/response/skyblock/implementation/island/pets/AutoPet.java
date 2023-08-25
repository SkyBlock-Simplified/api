package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pets;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutoPet {

    @SerializedName("rules_limit")
    @Getter private int rulesLimit;
    @Getter private ConcurrentList<Rule> rules = Concurrent.newList();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Rule {

        @Getter private String id;
        @SerializedName("pet")
        @Getter private String petName;
        @Getter private boolean disabled;
        @Getter private ConcurrentList<Exception> exceptions = Concurrent.newList();
        @Getter private ConcurrentMap<String, Object> data = Concurrent.newMap();

        public Optional<PetModel> getPet() {
            return SimplifiedApi.getRepositoryOf(PetModel.class).findFirst(PetModel::getKey, this.getPetName());
        }

        public static class Exception {

            @Getter private String id;
            @Getter private ConcurrentMap<String, Object> data = Concurrent.newMap();

        }

    }

}
