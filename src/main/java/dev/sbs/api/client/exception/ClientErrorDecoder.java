package dev.sbs.api.client.exception;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientErrorDecoder extends ErrorDecoder {

    @Override
    @NotNull ClientException decode(@NotNull String methodKey, @NotNull Response response);

    /**
     * Default decoder that creates basic ClientException instances.
     * Retry logic is handled automatically by InternalErrorDecoder.
     */
    final class Default implements ClientErrorDecoder {

        private @Nullable Integer maxBodyBytesLength;
        private @Nullable Integer maxBodyCharsLength;

        public Default() {
            this(null, null);
        }

        public Default(@Nullable Integer maxBodyBytesLength, @Nullable Integer maxBodyCharsLength) {
            this.maxBodyBytesLength = maxBodyBytesLength;
            this.maxBodyCharsLength = maxBodyCharsLength;
        }

        @Override
        public @NotNull ClientException decode(@NotNull String methodKey, @NotNull Response response) {
            return new ClientException(FeignException.errorStatus(
                methodKey,
                response,
                this.maxBodyBytesLength,
                this.maxBodyCharsLength
            ), response, "Client");
        }
    }


}
