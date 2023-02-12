package dev.sbs.api.client.discord.exception;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.HttpStatus;
import lombok.Getter;

public class DiscordErrorResponse {

    private int status;
    @SerializedName("statusTest")
    @Getter protected String reason;

    public HttpStatus getStatus() {
        return HttpStatus.getByCode(this.status);
    }

    public static class Unknown extends DiscordErrorResponse {

        public Unknown() {
            super.reason = "Unknown";
        }

    }

}
