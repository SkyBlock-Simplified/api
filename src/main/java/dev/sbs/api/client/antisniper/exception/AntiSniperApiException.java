package dev.sbs.api.client.antisniper.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.util.helper.StringUtil;
import feign.FeignException;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public final class AntiSniperApiException extends ApiException {

    @Getter private final AntiSniperErrorResponse errorResponse;

    public AntiSniperApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, AntiSniperErrorResponse.class);
        } else
            this.errorResponse = new AntiSniperErrorResponse.Unknown();
    }

}
