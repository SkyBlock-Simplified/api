package dev.sbs.api.client.antisniper;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.antisniper.exception.AntiSniperApiException;
import dev.sbs.api.client.antisniper.request.AntiSniperRequestInterface;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import feign.FeignException;
import feign.codec.ErrorDecoder;

import java.util.Map;

public final class AntiSniperApiBuilder extends ApiBuilder<AntiSniperRequestInterface> {

    public AntiSniperApiBuilder() {
        super("api.antisniper.net");
    }

    @Override
    public Map<String, String> getHeaders() {
        ConcurrentMap<String, String> headers = Concurrent.newMap();
        SimplifiedApi.getConfig().getAntiSniperApiKey().ifPresent(apiKey -> headers.put("API-Key", apiKey.toString()));
        return headers;
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new AntiSniperApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
