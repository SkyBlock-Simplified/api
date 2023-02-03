package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.shop_data.shop_profile_upgrades.ShopProfileUpgradeModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityUpgrades {

    @SerializedName("currently_upgrading")
    private Upgrading currentlyUpgrading;
    @SerializedName("upgrade_states")
    @Getter private ConcurrentList<Upgraded> upgraded;

    public Optional<Upgrading> getCurrentlyUpgrading() {
        return Optional.ofNullable(this.currentlyUpgrading);
    }

    public int getHighestTier(ShopProfileUpgradeModel shopProfileUpgradeModel) {
        return this.getUpgraded()
            .stream()
            .filter(upgraded -> upgraded.getUpgradeName().equalsIgnoreCase(shopProfileUpgradeModel.getKey()))
            .sorted((o1, o2) -> Comparator.comparing(Upgraded::getTier).compare(o2, o1))
            .map(Upgraded::getTier)
            .findFirst()
            .orElse(0);
    }

    public ConcurrentList<Upgraded> getUpgrades(ShopProfileUpgradeModel shopProfileUpgradeModel) {
        return this.getUpgraded()
            .stream()
            .filter(upgraded -> upgraded.getUpgradeName().equalsIgnoreCase(shopProfileUpgradeModel.getKey()))
            .sorted((o1, o2) -> Comparator.comparing(Upgraded::getTier).compare(o1, o2))
            .collect(Concurrent.toList());
    }

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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Upgraded {

        @SerializedName("upgrade")
        @Getter private String upgradeName;
        @Getter private int tier;
        @SerializedName("started_ms")
        @Getter private SkyBlockDate.RealTime started;
        @SerializedName("started_by")
        @Getter private String startedBy;
        @SerializedName("claimed_ms")
        @Getter private SkyBlockDate.RealTime claimed;
        @SerializedName("claimed_by")
        @Getter private String claimedBy;
        @SerializedName("fasttracked")
        @Getter private boolean fastTracked;

        public Optional<ShopProfileUpgradeModel> getUpgrade() {
            return SimplifiedApi.getRepositoryOf(ShopProfileUpgradeModel.class).findFirst(ShopProfileUpgradeModel::getKey, this.getUpgradeName().toUpperCase());
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Upgrading {

        @SerializedName("upgrade")
        @Getter private String upgradeName;
        @SerializedName("new_tier")
        @Getter private int newTier;
        @SerializedName("start_ms")
        @Getter private SkyBlockDate.RealTime started;
        @SerializedName("who_started")
        @Getter private String startedBy;

        public Optional<ShopProfileUpgradeModel> getUpgrade() {
            return SimplifiedApi.getRepositoryOf(ShopProfileUpgradeModel.class).findFirst(ShopProfileUpgradeModel::getKey, this.getUpgradeName().toUpperCase());
        }

    }

}
