package dev.sbs.api.client.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.Client;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.IHypixelRequest;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.data.tuple.pair.Pair;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

public final class HypixelClient extends Client<IHypixelRequest> {

    public HypixelClient() {
        super("api.hypixel.net");
    }

    @Override
    protected @NotNull ConcurrentMap<String, String> getRequestHeaders() {
        return Concurrent.newMap(Pair.of("API-Key", SimplifiedApi.getKeyManager().get("HYPIXEL_API_KEY").toString()));
    }

    @Override
    protected @NotNull ConcurrentSet<String> getResponseCacheHeaders() {
        return Concurrent.newUnmodifiableSet(
            "RateLimit-Limit",
            "RateLimit-Remaining",
            "RateLimit-Reset"
        );
    }

    @Override
    protected @NotNull ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
