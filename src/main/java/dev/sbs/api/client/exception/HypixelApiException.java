package dev.sbs.api.client.exception;

import feign.FeignException;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.hypixel.HypixelErrorResponse;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public class HypixelApiException extends ApiException {

    @Getter private final HypixelErrorResponse errorResponse;

    public HypixelApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, HypixelErrorResponse.class);
        } else
            this.errorResponse = null;
    }

}