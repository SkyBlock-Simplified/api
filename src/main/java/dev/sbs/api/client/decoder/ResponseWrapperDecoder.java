package dev.sbs.api.client.decoder;

import dev.sbs.api.client.metric.ConnectionDetails;
import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.stream.pair.Pair;
import feign.FeignException;
import feign.codec.Decoder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class ResponseWrapperDecoder implements Decoder {
    
    private final @NotNull Decoder delegate;
    private final @NotNull ConcurrentList<Response<?>> recentResponses;
    
    public ResponseWrapperDecoder(@NotNull Decoder delegate, @NotNull ConcurrentList<Response<?>> recentResponses) {
        this.delegate = delegate;
        this.recentResponses = recentResponses;
    }
    
    @Override
    public Object decode(@NotNull feign.Response feignResponse, @NotNull Type type) throws IOException, FeignException {
        Type bodyType = type;
        boolean shouldWrap = false;

        // Check if the return type is Response<T>
        if (type instanceof ParameterizedType parameterizedType) {
            if (parameterizedType.getRawType().equals(Response.class)) {
                shouldWrap = true;
                bodyType = parameterizedType.getActualTypeArguments()[0];
            }
        }

        // Decode body
        Object decodedBody = this.delegate.decode(feignResponse, bodyType);

        // Always build Response for caching
        Response<?> response = this.buildResponse(feignResponse, decodedBody);

        // Cache the response
        this.recentResponses.add(response);

        // Return wrapped or unwrapped based on method signature
        return shouldWrap ? response : decodedBody;
    }

    private @NotNull Response<?> buildResponse(@NotNull feign.Response feignResponse, Object body) {
        return new Response.Impl<>(
            body,
            new ConnectionDetails(feignResponse),
            HttpStatus.of(feignResponse.status()),
            new Request.Impl(
                HttpMethod.of(feignResponse.request().httpMethod().name()),
                feignResponse.request().url(),
                collectHeaders(feignResponse.request().headers())
            ),
            collectHeaders(feignResponse.headers())
        );
    }

    private static ConcurrentMap<String, ConcurrentList<String>> collectHeaders(@NotNull Map<String, Collection<String>> headers) {
        return headers.entrySet()
            .stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> !ConnectionDetails.isInternalHeader(entry.getKey()))
            .map(entry -> Pair.of(
                entry.getKey(),
                (ConcurrentList<String>) Concurrent.newUnmodifiableList(entry.getValue())
            ))
            .collect(Concurrent.toUnmodifiableMap());
    }

}
