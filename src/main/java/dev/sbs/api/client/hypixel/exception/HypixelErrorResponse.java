package dev.sbs.api.client.hypixel.exception;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.exception.ApiErrorResponse;
import lombok.Getter;

@Getter
public class HypixelErrorResponse implements ApiErrorResponse {

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
