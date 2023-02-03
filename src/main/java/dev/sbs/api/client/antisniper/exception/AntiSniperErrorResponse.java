package dev.sbs.api.client.antisniper.exception;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class AntiSniperErrorResponse {

    @Getter protected boolean success;
    @SerializedName("cause")
    @Getter protected String reason;

    public static class Unknown extends AntiSniperErrorResponse {

        public Unknown() {
            super.reason = "Unknown";
        }

    }

}
