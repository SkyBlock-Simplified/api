package dev.sbs.api.client.response;

import dev.sbs.api.util.ClientUtil;
import lombok.Getter;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

@Getter
public final class ConnectionDetails {

    private static final @NotNull String INTERNAL_HEADER_PREFIX = "X-Internal-";

    // Response
    public static final @NotNull String REQUEST_START = INTERNAL_HEADER_PREFIX + "Request-Start";
    public static final @NotNull String RESPONSE_RECEIVED = INTERNAL_HEADER_PREFIX + "Response-Received";

    // Latency
    public static final @NotNull String DNS_TIME = INTERNAL_HEADER_PREFIX + "DNS-Time";
    public static final @NotNull String TCP_CONNECT_TIME = INTERNAL_HEADER_PREFIX + "TCP-Connect-Time";
    public static final @NotNull String TLS_HANDSHAKE_TIME = INTERNAL_HEADER_PREFIX + "TLS-Handshake-Time";

    // TLS Info
    public static final @NotNull String TLS_PROTOCOL = INTERNAL_HEADER_PREFIX + "TLS-Protocol";
    public static final @NotNull String TLS_CIPHER = INTERNAL_HEADER_PREFIX + "TLS-Cipher";

    private final @NotNull Instant requestStart;
    private final @NotNull Instant responseReceived;
    private final long totalTime;
    private final long dnsTime;
    private final long tcpConnectTime;
    private final long tlsHandshakeTime;
    private final @NotNull Optional<String> tlsProtocol;
    private final @NotNull Optional<String> tlsCipher;

    public ConnectionDetails(@NotNull feign.Response response) {
        this.requestStart = ClientUtil.extractHeader(response.request().headers(), REQUEST_START)
            .map(Long::parseLong)
            .map(nanos -> Instant.ofEpochSecond(0, nanos))
            .orElse(Instant.now());

        this.responseReceived = ClientUtil.extractHeader(response.headers(), RESPONSE_RECEIVED)
            .map(Long::parseLong)
            .map(nanos -> Instant.ofEpochSecond(0, nanos))
            .orElse(Instant.now());

        this.totalTime = responseReceived.toEpochMilli() - requestStart.toEpochMilli();

        this.dnsTime = ClientUtil.extractHeader(response.request().headers(), DNS_TIME)
            .map(Long::parseLong)
            .orElse(0L);

        this.tcpConnectTime = ClientUtil.extractHeader(response.request().headers(), TCP_CONNECT_TIME)
            .map(Long::parseLong)
            .orElse(0L);

        this.tlsHandshakeTime = ClientUtil.extractHeader(response.request().headers(), TLS_HANDSHAKE_TIME)
            .map(Long::parseLong)
            .orElse(0L);

        this.tlsProtocol = ClientUtil.extractHeader(response.request().headers(), TLS_PROTOCOL);
        this.tlsCipher = ClientUtil.extractHeader(response.request().headers(), TLS_CIPHER);
    }

    public ConnectionDetails(@NotNull HttpContext context) {
        this.requestStart = Instant.ofEpochSecond(0L, ClientUtil.getAttribute(context, REQUEST_START, 0L));
        this.responseReceived = Instant.ofEpochSecond(0L, ClientUtil.getAttribute(context, RESPONSE_RECEIVED, 0L));
        this.totalTime = responseReceived.getNano() - requestStart.getNano();
        this.dnsTime = ClientUtil.getAttribute(context, DNS_TIME, 0L);
        this.tcpConnectTime = ClientUtil.getAttribute(context, TCP_CONNECT_TIME, 0L);
        this.tlsHandshakeTime = ClientUtil.getAttribute(context, TLS_HANDSHAKE_TIME, 0L);
        this.tlsProtocol = Optional.ofNullable(ClientUtil.getAttribute(context, TLS_PROTOCOL, null));
        this.tlsCipher = Optional.ofNullable(ClientUtil.getAttribute(context, TLS_CIPHER, null));
    }

    public static boolean isInternalHeader(@NotNull String headerName) {
        return headerName.startsWith(INTERNAL_HEADER_PREFIX);
    }

}