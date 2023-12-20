package dev.sbs.api.client.sbs.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class SbsApiException extends ApiException {

    private final @NotNull SbsErrorResponse errorResponse;

    public SbsApiException(@NotNull FeignException exception) {
        super(exception);

        this.errorResponse = this.getBody()
            .map(json -> SimplifiedApi.getGson().fromJson(json, SbsErrorResponse.class))
            .orElse(new SbsErrorResponse.Unknown());
    }

}
