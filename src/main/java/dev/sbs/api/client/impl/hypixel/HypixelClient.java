package dev.sbs.api.client.impl.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.Client;
import dev.sbs.api.client.impl.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.impl.hypixel.request.HypixelRequest;
import dev.sbs.api.client.response.CFCacheStatus;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.mutable.pair.Pair;
import feign.FeignException;

public final class HypixelClient extends Client<HypixelRequest> {

    public HypixelClient() {
        super("api.hypixel.net");
        super.setCachedResponseHeaders(Concurrent.newUnmodifiableSet(
            CFCacheStatus.HEADER_KEY,
            "RateLimit-Limit",
            "RateLimit-Remaining",
            "RateLimit-Reset"
        ));
        super.setErrorDecoder((methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        });
        super.setRequestHeaders(Concurrent.newUnmodifiableMap(
            Pair.of("API-Key", () -> SimplifiedApi.getKeyManager().get("HYPIXEL_API_KEY").toString())
        ));
    }

}
