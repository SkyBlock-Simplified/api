package dev.sbs.api.client.response;

import dev.sbs.api.util.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an HTTP response status code, providing standardized status codes
 * and messages as defined by the HTTP standard, along with additional custom
 * statuses for specific server or application errors.
 * <p>
 * Status codes are organized based on their numeric ranges and associated
 * categories, such as informational, success, redirection, client error, server error,
 * or other specialized states (e.g., network or proxy-specific errors).
 */
@Getter
public enum HttpStatus {

    CONTINUE(100),
    SWITCHING_PROTOCOLS(101),
    PROCESSING(102),
    EARLY_HINTS(103),
    OK(200, "OK"),
    CREATED(201),
    ACCEPTED(202),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204),
    RESET_CONTENT(205),
    PARTIAL_CONTENT(206),
    MULTI_STATUS(207, "Multi-Status"),
    ALREADY_REPORTED(208),
    IM_USED(226, "IM Used"),
    MULTIPLE_CHOICES(300),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    SWITCH_PROXY(306),
    TEMPORARY_REDIRECT(307),
    PERMANENT_REDIRECT(308),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    PRECONDITION_FAILED(412),
    REQUEST_ENTITY_TOO_LARGE(413),
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415),
    REQUESTED_RANGE_NOT_SATISFIABLE(416),
    EXPECTATION_FAILED(417),
    IM_A_TEAPOT(418, "I'm a teapot"),
    AUTHENTICATION_TIMEOUT(419),
    METHOD_FAILURE(420),
    MISDIRECTED_REQUEST(421),
    UNPROCESSABLE_ENTITY(422),
    LOCKED(423),
    FAILED_DEPENDENCY(424),
    TOO_EARLY(425),
    UPGRADE_REQUIRED(426),
    PRECONDITION_REQUIRED(428),
    TOO_MANY_REQUESTS(429),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431),
    LOGIN_TIMEOUT(440),
    NO_RESPONSE(444, HttpState.NGINX_ERROR),
    RETRY_WITH(449),
    BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS(450),
    UNAVAILABLE_FOR_LEGAL_REASONS(451),
    REQUEST_HEADER_TOO_LARGE(494, HttpState.NGINX_ERROR),
    SSL_CERTIFICATE_ERROR(495, HttpState.NGINX_ERROR),
    SSL_CERTIFICATE_REQUIRED(496, HttpState.NGINX_ERROR),
    HTTP_REQUEST_SENT_TO_HTTPS(497, "HTTP Request Sent to HTTPS", HttpState.NGINX_ERROR),
    CLIENT_CLOSED_REQUEST(499, HttpState.NGINX_ERROR),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506),
    INSUFFICIENT_STORAGE(507),
    LOOP_DETECTED(508),
    BANDWIDTH_LIMIT_EXCEEDED(509),
    NOT_EXTENDED(510),
    NETWORK_AUTHENTICATION_REQUIRED(511),
    CLOUDFLARE_WEB_SERVER_UNKNOWN_ERROR(520, "Web Server Returns An Unknown Error", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_WEB_SERVER_DOWN(521, "Web Server Is Down", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_CONNECTION_TIMED_OUT(522, "Connection Timed Out", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_ORIGIN_IS_UNREACHABLE(523, "Origin Is Unreachable", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_A_TIME_OUT_OCCURRED(524, "A Timeout Occurred", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_SSL_HANDSHAKE_FAILED(525, "SSL Handshake Failed", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_INVALID_SSL_CERTIFICATE(526, "Invalid SSL Certificate", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_RAILGUN_LISTENER_TO_ORIGIN_ERROR(527, "Railgun Listener To Origin Error", HttpState.CLOUDFLARE_ERROR),
    CLOUDFLARE_GENERIC_ERROR(530, "Generic Error", HttpState.CLOUDFLARE_ERROR),
    NETWORK_READ_TIMEOUT_ERROR(598, "Read Timeout Error", HttpState.NETWORK_ERROR),
    NETWORK_CONNECT_TIMEOUT_ERROR(599, "Connect Timeout Error", HttpState.NETWORK_ERROR),
    SOCKET_ERROR(990, HttpState.JAVA_ERROR),
    IO_ERROR(991, "IO Error", HttpState.JAVA_ERROR),
    UNKNOWN_ERROR(999, HttpState.JAVA_ERROR);

    private final int code;
    private final @NotNull String message;
    private final @NotNull HttpState state;

    HttpStatus(int code) {
        this(code, null, null);
    }

    HttpStatus(int code, @NotNull HttpState state) {
        this(code, null, state);
    }

    HttpStatus(int code, @Nullable String message) {
        this(code, message, null);
    }

    HttpStatus(int code, @Nullable String message, @Nullable HttpState state) {
        this.code = code;
        this.state = state != null ? state : HttpState.of(code);
        message = StringUtil.isEmpty(message) ? StringUtil.capitalizeFully(this.name().replace("_", " ")) : message;

        if (state != null)
            message = String.format("%s: %s", state.getTitle(), message);

        this.message = message;
    }

    /**
     * Retrieves the corresponding {@link HttpStatus} for the specified HTTP status code.
     * <p>
     * Iterates through all available {@link HttpStatus} values and returns the one
     * matching the given code. If no match is found, an {@link IllegalArgumentException}
     * is thrown stating the code is invalid.
     *
     * @param code the HTTP status code to find the corresponding {@link HttpStatus}
     * @return the {@link HttpStatus} associated with the provided code
     * @throws IllegalArgumentException if the provided code does not match any valid {@link HttpStatus}
     */
    public static @NotNull HttpStatus of(int code) {
        for (HttpStatus httpCode : HttpStatus.values()) {
            if (httpCode.getCode() == code)
                return httpCode;
        }

        throw new IllegalArgumentException("Invalid HTTP status code: " + code);
    }

}
