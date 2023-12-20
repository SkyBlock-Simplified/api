package dev.sbs.api.client.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.Client;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.HypixelRequest;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.pair.Pair;
import feign.FeignException;
import org.jetbrains.annotations.NotNull;

public final class HypixelClient extends Client<HypixelRequest> {

    public HypixelClient() {
        super("api.hypixel.net");
        super.setCachedResponseHeaders(Concurrent.newUnmodifiableSet(
            "CF-Cache-Status",
            "RateLimit-Limit",
            "RateLimit-Remaining",
            "RateLimit-Reset"
        ));
        super.setErrorDecoder((methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        });
    }

    @Override
    protected @NotNull ConcurrentMap<String, String> getRequestHeaders() {
        return Concurrent.newUnmodifiableMap(
            Pair.of("API-Key", SimplifiedApi.getKeyManager().get("HYPIXEL_API_KEY").toString())
        );
    }

}
