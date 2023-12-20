package dev.sbs.api.client.impl.mojang.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class MojangApiException extends ApiException {

    private final @NotNull MojangErrorResponse errorResponse;

    public MojangApiException(@NotNull FeignException exception) {
        super(exception);

        this.errorResponse = this.getBody()
            .map(json -> SimplifiedApi.getGson().fromJson(json, MojangErrorResponse.class))
            .orElse(new MojangErrorResponse.Unknown());
    }

}
