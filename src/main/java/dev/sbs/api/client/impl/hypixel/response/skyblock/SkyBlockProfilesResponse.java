package dev.sbs.api.client.impl.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class SkyBlockProfilesResponse {

    private boolean success;
    @SerializedName("profiles")
    private @NotNull ConcurrentList<SkyBlockIsland> islands = Concurrent.newList();

    public @NotNull Optional<SkyBlockIsland> getIsland(@NotNull String profileName) {
        return this.getIslands()
            .stream()
            .filter(skyBlockIsland -> skyBlockIsland.getProfileName()
                .map(islandName -> islandName.equalsIgnoreCase(profileName))
                .orElse(false)
            )
            .findFirst();
    }

    public @NotNull SkyBlockIsland getSelected() {
        return this.getIslands()
            .stream()
            .filter(SkyBlockIsland::isSelected)
            .findFirst()
            .orElse(this.getIslands().get(0));
    }

}
