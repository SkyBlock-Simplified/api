package dev.sbs.api.client.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.HypixelRequestInterface;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.data.tuple.Pair;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public final class HypixelApiBuilder extends ApiBuilder<HypixelRequestInterface> {

    @Getter private UUID apiKey;

    public HypixelApiBuilder() {
        super("api.hypixel.net");
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return Concurrent.newMap(Pair.of("API-Key", SimplifiedApi.getKeyManager().get("HYPIXEL_API_KEY").toString()));
    }

    @Override
    protected ConcurrentSet<String> getResponseCacheHeaders() {
        return Concurrent.newUnmodifiableSet(
            "RateLimit-Limit",
            "RateLimit-Remaining",
            "RateLimit-Reset"
        );
    }

    @Override
    protected ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
