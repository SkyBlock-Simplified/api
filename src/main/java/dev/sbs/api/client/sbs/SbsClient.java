package dev.sbs.api.client.sbs;

import dev.sbs.api.client.Client;
import dev.sbs.api.client.sbs.exception.SbsApiException;
import dev.sbs.api.client.sbs.request.SbsRequest;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

public final class SbsClient extends Client<SbsRequest> {

    public SbsClient() {
        super("api.sbs.dev");
    }

    @Override
    protected @NotNull ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new SbsApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
