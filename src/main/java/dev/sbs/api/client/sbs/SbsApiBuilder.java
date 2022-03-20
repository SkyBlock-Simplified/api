package dev.sbs.api.client.sbs;

import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.sbs.exception.SbsApiException;
import feign.FeignException;
import feign.codec.ErrorDecoder;

public final class SbsApiBuilder extends ApiBuilder<SbsRequestInterface> {

    public SbsApiBuilder() {
        super("api.sbs.dev");
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new SbsApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
