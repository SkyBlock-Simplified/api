package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockProfilesResponse {

    @Getter private boolean success;
    @SerializedName("profiles")
    @Getter private ConcurrentList<SkyBlockIsland> islands = Concurrent.newList();

    public Optional<SkyBlockIsland> getIsland(@NotNull ProfileModel profileModel) {
        return this.getIslands()
            .stream()
            .filter(skyBlockIsland -> skyBlockIsland.getProfileModel().map(profileModel::equals).orElse(false))
            .findFirst();
    }

    public SkyBlockIsland getLastPlayed() {
        return this.getIslands()
            .stream()
            .filter(skyBlockIsland -> skyBlockIsland.getLastSave().isPresent())
            .max(Comparator.comparing(
                SearchFunction.combine(
                    SearchFunction.combine(SkyBlockIsland::getLastSave, Optional::get),
                    SkyBlockDate::getRealTime
                )
            ))
            .orElse(this.getIslands().get(0));
    }

}
