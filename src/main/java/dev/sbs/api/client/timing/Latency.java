package dev.sbs.api.client.timing;

import lombok.Getter;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

@Getter
public final class Latency {

    // Timing Keys
    public static final @NotNull String DNS_TIME = "latency.dns_time";
    public static final @NotNull String TCP_CONNECT_TIME = "latency.tcp_connect_time";
    public static final @NotNull String TLS_HANDSHAKE_TIME = "latency.tls_handshake_time";
    public static final @NotNull String REQUEST_START = "latency.request_start";
    public static final @NotNull String RESPONSE_RECEIVED = "latency.response_received";

    // TLS Info
    public static final @NotNull String TLS_PROTOCOL = "connection.tls_protocol";
    public static final @NotNull String TLS_CIPHER = "connection.tls_cipher";

    private final @NotNull Instant requestStart;
    private final @NotNull Instant responseReceived;
    private final long total;
    private final long dnsTime;
    private final long tcpConnectTime;
    private final long tlsHandshakeTime;
    private final @NotNull Optional<String> tlsProtocol;
    private final @NotNull Optional<String> tlsCipher;

    public Latency(@NotNull HttpContext context) {
        this.requestStart = Instant.ofEpochSecond(0L, getAttribute(context, REQUEST_START, 0L));
        this.responseReceived = Instant.ofEpochSecond(0L, getAttribute(context, RESPONSE_RECEIVED, 0L));
        this.total = responseReceived.getNano() - requestStart.getNano();
        this.dnsTime = getAttribute(context, DNS_TIME, 0L);
        this.tcpConnectTime = getAttribute(context, TCP_CONNECT_TIME, 0L);
        this.tlsHandshakeTime = getAttribute(context, TLS_HANDSHAKE_TIME, 0L);
        this.tlsProtocol = Optional.ofNullable(getAttribute(context, TLS_PROTOCOL, null));
        this.tlsCipher = Optional.ofNullable(getAttribute(context, TLS_CIPHER, null));
    }

    @SuppressWarnings("unchecked")
    private static <T> T getAttribute(@NotNull HttpContext context, @NotNull String id, T defaultValue) {
        return context.getAttribute(id) != null ? (T) context.getAttribute(id) : defaultValue;
    }

}