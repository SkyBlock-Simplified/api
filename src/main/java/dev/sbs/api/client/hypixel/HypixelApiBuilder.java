package dev.sbs.api.client.hypixel;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.implementation.HypixelRequestInterface;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import dev.sbs.api.client.ApiBuilder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public final class HypixelApiBuilder extends ApiBuilder<HypixelRequestInterface> {

    public static final Pattern apiKeyRegex = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");
    @Getter private UUID apiKey;

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
