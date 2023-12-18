package dev.sbs.api.client.mojang.exception;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class MojangErrorResponse {

    @Getter protected boolean success;
    @SerializedName("errorMessage")
    @Getter protected String reason;
    protected String path;

    public static class Unknown extends MojangErrorResponse {

        public Unknown() {
            super.success = false;
            super.reason = "Unknown";
        }

    }

}
