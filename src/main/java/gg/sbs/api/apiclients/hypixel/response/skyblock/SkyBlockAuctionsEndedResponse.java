package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionsEndedResponse {

    @Getter private boolean success;
    @Getter private SkyBlockDate.RealTime lastUpdated;

    public static class EndedAuction {

        @SerializedName("auction_id")
        private String auctionId;
        @SerializedName("seller")
        private String sellerId;
        @SerializedName("seller_profile")
        private String sellerIslandId;
        @SerializedName("buyer")
        private String buyerId;
        @Getter private SkyBlockDate.RealTime timestamp;
        @Getter private long price;
        @Getter private boolean bin;
        @SerializedName("item_bytes")
        @Getter SkyBlockIsland.NbtContent itemNbt;

    }

}