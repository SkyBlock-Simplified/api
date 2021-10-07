package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuction {

    @SerializedName("uuid")
    private String auctionId;
    @SerializedName("auctioneer")
    private String auctioneerId;
    @SerializedName("profile_id")
    private String islandId;
    @SerializedName("coop")
    private ConcurrentList<String> coopMembers = Concurrent.newList();

    @SerializedName("start")
    @Getter
    private SkyBlockDate.RealTime startedAt;
    @SerializedName("end")
    @Getter private SkyBlockDate.RealTime endsAt;
    @SerializedName("item_lore")
    @Getter private String lore; // TODO: Split by \n into list
    @Getter private String extra;
    @SerializedName("tier")
    @Getter private String rarity; // TODO: Convert to RarityModel?
    @SerializedName("starting_big")
    @Getter private long startingBid;
    @SerializedName("item_bytes")
    @Getter SkyBlockIsland.NbtContent itemNbt;
    @Getter private boolean claimed;
    @SerializedName("claimed_bidders")
    @Getter private ConcurrentList<Bid> claimedBidders = Concurrent.newList(); // TODO: Need sample data
    @SerializedName("highest_bid_amount")
    @Getter private long highestBid;
    @Getter private ConcurrentList<Bid> bids = Concurrent.newList();
    @Getter private boolean bin;

    public UUID getAuctionId() {
        return StringUtil.toUUID(this.auctionId);
    }

    public UUID getAuctioneerId() {
        return StringUtil.toUUID(this.auctioneerId);
    }

    public ConcurrentList<UUID> getCoopMembers() {
        return this.coopMembers.stream().map(StringUtil::toUUID).collect(Concurrent.toList());
    }

    public UUID getIslandId() {
        return StringUtil.toUUID(this.islandId);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bid {

        @SerializedName("auction_id")
        private String auctionId;
        @SerializedName("bidder")
        private String bidderId;
        @SerializedName("profile_id")
        private String islandId;

        @Getter private long amount;
        @Getter private SkyBlockDate.RealTime timestamp;

        public UUID getAuctionId() {
            return StringUtil.toUUID(this.auctionId);
        }

        public UUID getBidderId() {
            return StringUtil.toUUID(this.bidderId);
        }

        public UUID getIslandId() {
            return StringUtil.toUUID(this.islandId);
        }

    }

}