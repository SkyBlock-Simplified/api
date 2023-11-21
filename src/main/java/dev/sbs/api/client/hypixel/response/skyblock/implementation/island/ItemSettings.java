package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSettings {

    @SerializedName("teleporter_pill_consumed")
    private boolean teleporterPillConsumed;
    @SerializedName("favorite_arrow")
    private String favoriteArrow;
    private int soulflow;

}
