package dev.sbs.api.client.antisniper;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.antisniper.exception.AntiSniperApiException;
import dev.sbs.api.client.antisniper.request.AntiSniperRequestInterface;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import feign.FeignException;
import feign.codec.ErrorDecoder;

public final class AntiSniperApiBuilder extends ApiBuilder<AntiSniperRequestInterface> {

    public AntiSniperApiBuilder() {
        super("api.antisniper.net");
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new AntiSniperApiException(FeignException.errorStatus(methodKey, response));
        };
    }

    @Override
    public ConcurrentMap<String, ConcurrentList<String>> getQueries() {
        ConcurrentMap<String, ConcurrentList<String>> queries = Concurrent.newMap();
        SimplifiedApi.getConfig().getAntiSniperApiKey().ifPresent(apiKey -> queries.put("key", Concurrent.newList(apiKey.toString())));
        return queries;
    }

}
