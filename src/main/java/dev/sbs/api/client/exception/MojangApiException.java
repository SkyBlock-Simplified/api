package dev.sbs.api.client.exception;

import feign.FeignException;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.mojang.response.MojangErrorResponse;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public class MojangApiException extends ApiException {

    @Getter private final MojangErrorResponse errorResponse;

    public MojangApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, MojangErrorResponse.class);
        } else
            this.errorResponse = null;
    }

}
