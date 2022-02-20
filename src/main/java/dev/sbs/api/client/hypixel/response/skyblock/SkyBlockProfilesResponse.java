package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockProfilesResponse {

    @Getter private boolean success;
    @SerializedName("profiles")
    @Getter private ConcurrentList<SkyBlockIsland> islands = Concurrent.newList();

    public final Optional<SkyBlockIsland> getIsland(@NotNull ProfileModel profileModel) {
        return this.getIslands()
            .stream()
            .filter(skyBlockIsland -> skyBlockIsland.getProfileName().map(profileModel::equals).orElse(false))
            .findFirst();
    }

}
