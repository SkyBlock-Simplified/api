package gg.sbs.api.apiclients.mojang;

import feign.FeignException;
import feign.codec.ErrorDecoder;
import gg.sbs.api.apiclients.ApiBuilder;
import gg.sbs.api.apiclients.exception.MojangApiException;
import gg.sbs.api.apiclients.mojang.implementation.MojangRequestInterface;

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