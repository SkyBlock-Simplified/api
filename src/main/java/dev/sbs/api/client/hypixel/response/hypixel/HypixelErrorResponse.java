package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class HypixelErrorResponse {

    @Getter private boolean success;
    @SerializedName("cause")
    @Getter private String reason;
    @Getter private boolean throttle;
    @Getter private boolean global;

}