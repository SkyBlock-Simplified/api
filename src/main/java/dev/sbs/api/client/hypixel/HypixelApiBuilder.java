package dev.sbs.api.client.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.HypixelRequestInterface;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public final class HypixelApiBuilder extends ApiBuilder<HypixelRequestInterface> {

    @Getter
    private UUID apiKey;

    public HypixelApiBuilder() {
        super("api.hypixel.net");
    }

    @Override
    public Map<String, String> getHeaders() {
        ConcurrentMap<String, String> headers = Concurrent.newMap();
        SimplifiedApi.getConfig().getHypixelApiKey().ifPresent(apiKey -> headers.put("API-Key", apiKey.toString()));
        return headers;
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
