package dev.sbs.api.client.impl.sbs.exception;

import dev.sbs.api.client.exception.ApiException;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class SbsApiException extends ApiException {

    private final @NotNull SbsErrorResponse response;

    public SbsApiException(@NotNull FeignException exception) {
        super(exception, "Sbs");
        this.response = this.getBody()
            .map(json -> super.fromJson(json, SbsErrorResponse.class))
            .orElse(new SbsErrorResponse.Unknown());
    }

}
