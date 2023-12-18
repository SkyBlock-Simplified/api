package dev.sbs.api.client.hypixel.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.util.helper.StringUtil;
import feign.FeignException;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public final class HypixelApiException extends ApiException {

    private final HypixelErrorResponse errorResponse;

    public HypixelApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, HypixelErrorResponse.class);
        } else
            this.errorResponse = new HypixelErrorResponse.Unknown();
    }

}
