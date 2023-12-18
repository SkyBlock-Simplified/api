package dev.sbs.api.client.sbs.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.util.helper.StringUtil;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@Getter
public final class SbsApiException extends ApiException {

    private final @NotNull SbsErrorResponse errorResponse;

    public SbsApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, SbsErrorResponse.class);
        } else
            this.errorResponse = new SbsErrorResponse.Unknown();
    }

}
