package dev.sbs.api.client.impl.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.impl.hypixel.response.hypixel.implementation.HypixelSession;
import lombok.Getter;

import java.util.UUID;

@Getter
public class HypixelStatusResponse {

    private boolean success;
    @SerializedName("uuid")
    private UUID uniqueId;
    private HypixelSession session;

}
