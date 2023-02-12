package dev.sbs.api.client.discord.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.util.helper.StringUtil;
import feign.FeignException;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public final class DiscordApiException extends ApiException {

    @Getter private final DiscordErrorResponse errorResponse;

    public DiscordApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, DiscordErrorResponse.class);
        } else
            this.errorResponse = new DiscordErrorResponse.Unknown();
    }

}
