package dev.sbs.api.client.decoder;

import dev.sbs.api.client.exception.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

/**
 * A decoder for translating HTTP client errors into structured {@code ApiException} instances.
 */
public interface ClientErrorDecoder extends ErrorDecoder {

    @Override
    @NotNull ApiException decode(@NotNull String methodKey, @NotNull Response response);

}
