package dev.sbs.api.client.sbs;

import dev.sbs.api.client.Client;
import dev.sbs.api.client.sbs.exception.SbsApiException;
import dev.sbs.api.client.sbs.request.SbsRequest;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import feign.FeignException;

public final class SbsClient extends Client<SbsRequest> {

    public SbsClient() {
        super("api.sbs.dev");
        super.setCachedResponseHeaders(Concurrent.newUnmodifiableSet("CF-Cache-Status"));
        super.setErrorDecoder((methodKey, response) -> {
            throw new SbsApiException(FeignException.errorStatus(methodKey, response));
        });
    }

}
