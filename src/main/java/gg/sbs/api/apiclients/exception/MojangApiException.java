package gg.sbs.api.apiclients.exception;

import feign.FeignException;
import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.apiclients.mojang.response.MojangErrorResponse;
import gg.sbs.api.util.StringUtil;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public class MojangApiException extends ApiException {

    @Getter
    private final MojangErrorResponse errorResponse;

    public MojangApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, MojangErrorResponse.class);
        } else
            this.errorResponse = null;
    }

}