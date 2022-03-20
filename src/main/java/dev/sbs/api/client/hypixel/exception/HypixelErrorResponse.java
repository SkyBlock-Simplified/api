package dev.sbs.api.client.hypixel.exception;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class HypixelErrorResponse {

    @Getter protected boolean success;
    @SerializedName("cause")
    @Getter protected String reason;
    @Getter private boolean throttle;
    @Getter private boolean global;

    public static class Unknown extends HypixelErrorResponse {

        public Unknown() {
            super.success = false;
            super.reason = "Unknown";
        }

    }

}
