package dev.sbs.api.client.mojang.exception;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class MojangErrorResponse {

    protected boolean success;
    @SerializedName("error")
    protected String type;
    @SerializedName("errorMessage")
    protected String reason;
    protected String path;

    public static class Unknown extends MojangErrorResponse {

        public Unknown() {
            super.success = false;
            super.type = "UNKNOWN";
            super.reason = "Unknown Reason";
            super.path = "";
        }

    }

}
