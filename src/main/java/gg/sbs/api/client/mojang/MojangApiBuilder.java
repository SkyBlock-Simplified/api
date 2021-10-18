package gg.sbs.api.client.mojang;

import feign.FeignException;
import feign.codec.ErrorDecoder;
import gg.sbs.api.client.ApiBuilder;
import gg.sbs.api.client.exception.MojangApiException;
import gg.sbs.api.client.mojang.implementation.MojangRequestInterface;

public final class MojangApiBuilder extends ApiBuilder<MojangRequestInterface> {

    public MojangApiBuilder() {
        super("api.skyblocksimplified.com");
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new MojangApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}