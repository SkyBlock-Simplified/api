package dev.sbs.api.client.decoder;

import dev.sbs.api.client.request.HttpMethod;
import dev.sbs.api.client.request.Request;
import dev.sbs.api.client.response.ConnectionDetails;
import dev.sbs.api.client.response.HttpStatus;
import dev.sbs.api.client.response.Response;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import feign.FeignException;
import feign.codec.Decoder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Responsible for decoding responses received from Feign clients.
 * It acts as a wrapper around another {@link Decoder}, adding functionality for handling and storing recent decoded
 * responses for monitoring or debugging purposes.
 * <p>
 * This class allows for decoding of both standard object types and parameterized {@code Response<T>} types. If a
 * response type is parameterized, the decoded object will be wrapped into a {@link Response} instance with additional
 * metadata such as HTTP status, headers, and connection details. The decoded responses are added to a
 * thread-safe {@link ConcurrentList} for future reference.
 *
 * @apiNote Only for internal use.
 */
public final class InternalResponseDecoder implements Decoder {
    
    private final @NotNull Decoder delegate;
    private final @NotNull ConcurrentList<Response<?>> recentResponses;
    
    public InternalResponseDecoder(@NotNull Decoder delegate, @NotNull ConcurrentList<Response<?>> recentResponses) {
        this.delegate = delegate;
        this.recentResponses = recentResponses;
    }
    
    @Override
    public Object decode(@NotNull feign.Response feignResponse, @NotNull Type type) throws IOException, FeignException {
        Type bodyType = type;
        boolean shouldWrap = false;

        if (type instanceof ParameterizedType parameterizedType) {
            if (parameterizedType.getRawType().equals(Response.class)) {
                shouldWrap = true;
                bodyType = parameterizedType.getActualTypeArguments()[0];
            }
        }

        Object decodedBody = this.delegate.decode(feignResponse, bodyType);
        Response<?> response = this.buildResponse(feignResponse, decodedBody);
        this.recentResponses.add(response);
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
                Response.getHeaders(feignResponse.request().headers())
            ),
            Response.getHeaders(feignResponse.headers())
        );
    }

}
