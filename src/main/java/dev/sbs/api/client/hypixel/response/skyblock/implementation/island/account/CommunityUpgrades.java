package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class CommunityUpgrades {

    @SerializedName("currently_upgrading")
    private @NotNull Optional<Upgrading> currentlyUpgrading = Optional.empty();
    @SerializedName("upgrade_states")
    private @NotNull ConcurrentList<Upgraded> upgraded = Concurrent.newList();

    public enum Upgrade {

        @SerializedName("minion_slots")
        MINION_SLOTS,
        @SerializedName("coins_allowance")
        COINS_ALLOWANCE,
        @SerializedName("guests_count")
        GUESTS_COUNT,
        @SerializedName("island_size")
        ISLAND_SIZE,
        @SerializedName("coop_slots")
        COOP_SLOTS

    }

    @Getter
    public static class Upgraded {

        @SerializedName("upgrade")
        private String upgradeName;
        private int tier;
        @SerializedName("started_ms")
        private SkyBlockDate.RealTime started;
        @SerializedName("started_by")
        private String startedBy;
        @SerializedName("claimed_ms")
        private SkyBlockDate.RealTime claimed;
        @SerializedName("claimed_by")
        private String claimedBy;
        @SerializedName("fasttracked")
        private boolean fastTracked;

    }

    @Getter
    public static class Upgrading {

        @SerializedName("upgrade")
        private String upgradeName;
        @SerializedName("new_tier")
        private int newTier;
        @SerializedName("start_ms")
        private SkyBlockDate.RealTime started;
        @SerializedName("who_started")
        private String startedBy;

    }

}
