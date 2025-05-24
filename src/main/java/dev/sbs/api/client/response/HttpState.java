package dev.sbs.api.client.response;

import dev.sbs.api.util.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the HTTP status ranges and specific application-related states.
 * <p>
 * Each state optionally defines a range of HTTP status codes that it represents.
 * <ul>
 *     <li>INFORMATIONAL - Represents HTTP informational response status codes ({@code 1xx}).</li>
 *     <li>SUCCESS - Represents HTTP success status codes ({@code 2xx}).</li>
 *     <li>REDIRECTION - Represents HTTP redirection status codes ({@code 3xx}).</li>
 *     <li>CLIENT_ERROR - Represents errors occurred on the client-side ({@code 4xx}).</li>
 *     <li>SERVER_ERROR - Represents HTTP server-side error codes ({@code 5xx}).</li>
 *     <li>NGINX_ERROR - Specifically represents errors associated with Nginx servers ({@code 444, 494-499}).</li>
 *     <li>CLOUDFLARE_ERROR - Specifically represents errors associated with Cloudflare services ({@code 520-530}).</li>
 *     <li>NETWORK_ERROR - Represents errors related to network issues ({@code 598-599}).</li>
 *     <li>JAVA_ERROR - Represents errors specific to the Java system ({@code 990-999}).</li>
 * </ul>
 */
@Getter
public enum HttpState {

    INFORMATIONAL(100, 199),
    SUCCESS(200, 299),
    REDIRECTION(300, 399),
    CLIENT_ERROR(400, 451),
    // Server Errors
    SERVER_ERROR(500, 599, true),
    NGINX_ERROR(494, 499, true),
    CLOUDFLARE_ERROR(520, 530, true),
    // Application-specific Errors
    NETWORK_ERROR(598, 599, true),
    JAVA_ERROR(990, 999, true);

    private final @NotNull String title;
    private final int minCode;
    private final int maxCode;
    private final boolean error;

    HttpState(int minCode, int maxCode) {
        this(minCode, maxCode, false);
    }

    HttpState(int minCode, int maxCode, boolean error) {
        this.title = StringUtil.capitalizeFully(this.name().replace("_", " "));
        this.minCode = minCode;
        this.maxCode = maxCode;
        this.error = error;
    }

    /**
     * Checks if the given HTTP status code belongs to this state's range.
     *
     * @param code the HTTP status code to check
     * @return true if the code falls within this state's range
     */
    public boolean containsCode(int code) {
        return code >= this.getMinCode() && code <= this.getMaxCode();
    }

    /**
     * Checks if this state represents a successful outcome.
     *
     * @return true if this is a success state
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }

    /**
     * Retrieves the {@link HttpState} corresponding to the provided HTTP status code.
     * <p>
     * This method iterates through all available {@link HttpState} values to find the state
     * that contains the given code within its specified range. If the code falls within the
     * range of {@link HttpState#SERVER_ERROR}, it returns that state. If no match is found,
     * it throws an {@link IllegalArgumentException}.
     *
     * @param code the HTTP status code to locate the corresponding {@link HttpState}
     * @return the {@link HttpState} corresponding to the given code
     * @throws IllegalArgumentException if the provided code does not match any valid {@link HttpState}
     */
    public static @NotNull HttpState of(int code) {
        for (HttpState httpState : values()) {
            if (httpState == SERVER_ERROR)
                continue;

            if (httpState.containsCode(code))
                return httpState;
        }

        if (SERVER_ERROR.containsCode(code))
            return SERVER_ERROR;

        throw new IllegalArgumentException("Invalid HTTP status code: " + code);
    }

}
