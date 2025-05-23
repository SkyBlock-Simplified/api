package dev.sbs.api.client.impl.hypixel.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class HypixelApiException extends ApiException {

    private final @NotNull HypixelErrorResponse response;

    public HypixelApiException(@NotNull FeignException exception) {
        super(exception, "Hypixel");
        this.response = this.getBody()
            .map(json -> SimplifiedApi.getGson().fromJson(json, HypixelErrorResponse.class))
            .orElse(new HypixelErrorResponse.Unknown());
    }

}
