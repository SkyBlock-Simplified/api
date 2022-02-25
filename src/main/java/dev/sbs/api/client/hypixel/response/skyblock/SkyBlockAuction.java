package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.UUID;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuction {

    @SerializedName("item_bytes")
    @Getter SkyBlockIsland.NbtContent itemNbt;
    @SerializedName("uuid")
    @Getter private UUID auctionId;
    @SerializedName("auctioneer")
    @Getter private UUID auctioneerId;
    @SerializedName("profile_id")
    @Getter private UUID islandId;
    @SerializedName("coop")
    private ConcurrentList<String> coopMembers = Concurrent.newList();
    @SerializedName("start")
    @Getter private SkyBlockDate.RealTime startedAt;
    @SerializedName("end")
    @Getter private SkyBlockDate.RealTime endsAt;
    @SerializedName("item_lore")
    private String lore;
    @Getter private String extra;
    @SerializedName("tier")
    private String rarity;
    @SerializedName("starting_big")
    @Getter private long startingBid;
    @Getter private boolean claimed;
    @SerializedName("claimed_bidders")
    @Getter private ConcurrentList<Bid> claimedBidders = Concurrent.newList(); // TODO: Need sample data
    @SerializedName("highest_bid_amount")
    @Getter private long highestBid;
    @Getter private ConcurrentList<Bid> bids = Concurrent.newList();
    @Getter private boolean bin;

    public ConcurrentList<UUID> getCoopMembers() {
        return this.coopMembers.stream().map(StringUtil::toUUID).collect(Concurrent.toList());
    }

    public ConcurrentList<String> getLore() {
        return Concurrent.newUnmodifiableList(StringUtil.split(this.lore, '\n'));
    }

    @SneakyThrows
    public RarityModel getRarity() {
        return SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getKey, this.rarity);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bid {

        @SerializedName("auction_id")
        @Getter UUID auctionId;
        @SerializedName("bidder")
        @Getter private UUID bidderId;
        @SerializedName("profile_id")
        @Getter private UUID islandId;
        @Getter private long amount;
        @Getter private SkyBlockDate.RealTime timestamp;

    }

}
