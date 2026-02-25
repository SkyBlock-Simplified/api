package dev.sbs.api.client.decoder;

import dev.sbs.api.client.exception.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code ClientErrorDecoder} interface is responsible for processing and decoding client-side
 * HTTP error responses into {@link ApiException} objects. It extends the {@link ErrorDecoder}
 * interface and provides a specialized implementation for handling client-side errors.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Decoding HTTP responses received from client-side requests into {@link ApiException}.</li>
 *   <li>Providing consistent error transformation for HTTP errors based on method-specific handling.</li>
 *   <li>Serving as a contract for defining custom client error decoding behavior.</li>
 * </ul>
 *
 * <h2>Integration</h2>
 * Implementing classes should define the behavior for decoding client-side HTTP error responses
 * into appropriate {@link ApiException} instances for use within application error handling mechanisms.
 *
 * <h2>Method Behavior</h2>
 * <ul>
 *   <li>The {@code decode} method receives the method key and response as inputs.</li>
 *   <li>It must interpret the response and produce an {@link ApiException} object encapsulating
 *       the relevant error information.</li>
 *   <li>Custom logic may be applied based on the method key or response content.</li>
 * </ul>
 */
public interface ClientErrorDecoder extends ErrorDecoder {

    @Override
    @NotNull ApiException decode(@NotNull String methodKey, @NotNull Response response);

}
