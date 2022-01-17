package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionsEndedResponse {

    @Getter private boolean success;
    @Getter private SkyBlockDate.RealTime lastUpdated;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EndedAuction {

        @SerializedName("auction_id")
        private UUID auctionId;
        @SerializedName("seller")
        private UUID sellerId;
        @SerializedName("seller_profile")
        private UUID sellerIslandId;
        @SerializedName("buyer")
        private UUID buyerId;
        @Getter private SkyBlockDate.RealTime timestamp;
        @Getter private long price;
        @Getter private boolean bin;
        @SerializedName("item_bytes")
        @Getter SkyBlockIsland.NbtContent itemNbt;

    }

}
