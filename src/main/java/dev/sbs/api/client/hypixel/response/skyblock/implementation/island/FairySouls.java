package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FairySouls {

    @SerializedName("total_collected")
    private int totalCollected;
    @SerializedName("fairy_exchanges")
    private int exchanges;
    @SerializedName("unspent_souls")
    private int unspent;

}
