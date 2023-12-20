package dev.sbs.api.client.impl.hypixel.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HypixelApiException extends ApiException {

    private final @NotNull HypixelErrorResponse errorResponse;

    public HypixelApiException(@NotNull FeignException exception) {
        super(exception);

        this.errorResponse = this.getBody()
            .map(json -> SimplifiedApi.getGson().fromJson(json, HypixelErrorResponse.class))
            .orElse(new HypixelErrorResponse.Unknown());
    }

}
