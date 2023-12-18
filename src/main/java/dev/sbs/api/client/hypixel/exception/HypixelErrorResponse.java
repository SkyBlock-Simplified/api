package dev.sbs.api.client.hypixel.exception;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class HypixelErrorResponse {

    protected boolean success;
    @SerializedName("cause")
    protected String reason;
    protected boolean throttle;
    protected boolean global;

    public static class Unknown extends HypixelErrorResponse {

        public Unknown() {
            super.success = false;
            super.reason = "Unknown";
        }

    }

}
