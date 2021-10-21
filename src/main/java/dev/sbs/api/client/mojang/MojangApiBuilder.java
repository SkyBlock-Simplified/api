package dev.sbs.api.client.mojang;

import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.exception.MojangApiException;
import dev.sbs.api.client.mojang.implementation.MojangRequestInterface;
import feign.FeignException;
import feign.codec.ErrorDecoder;

public final class MojangApiBuilder extends ApiBuilder<MojangRequestInterface> {

    public MojangApiBuilder() {
        super("api.sbs.dev");
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new MojangApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}