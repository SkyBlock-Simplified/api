package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.date.CustomDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

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

    public SkyBlockIsland getLastPlayed(UUID uniqueId) {
        Optional<SkyBlockIsland> lastPlayedIsland = Optional.empty();
        long lastPlayed = 0;

        for (SkyBlockIsland skyBlockIsland : this.getIslands()) {
            if (!lastPlayedIsland.isPresent()) {
                lastPlayedIsland = Optional.of(skyBlockIsland);
                lastPlayed = skyBlockIsland.getMember(uniqueId)
                    .map(SkyBlockIsland.Member::getLastSave)
                    .map(SkyBlockDate::getRealTime)
                    .orElse(0L);
                continue;
            }

            Optional<SkyBlockIsland.Member> optionalMember = skyBlockIsland.getMember(uniqueId);

            if (optionalMember.isPresent()) {
                SkyBlockIsland.Member member = optionalMember.get();
                long memberLastPlayed = Optional.ofNullable(member.getLastSave()).map(CustomDate::getRealTime).orElse(-1L);

                if (memberLastPlayed > lastPlayed) {
                    lastPlayedIsland = Optional.of(skyBlockIsland);
                    lastPlayed = memberLastPlayed;
                }
            }
        }

        return lastPlayedIsland.orElse(this.getIslands().get(0));
    }

}
