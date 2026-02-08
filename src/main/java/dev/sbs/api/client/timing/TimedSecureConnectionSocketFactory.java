package dev.sbs.api.client.timing;

import org.apache.http.HttpHost;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class TimedSecureConnectionSocketFactory implements LayeredConnectionSocketFactory {

    private final @NotNull LayeredConnectionSocketFactory delegate;
    private final @NotNull DnsResolver dnsResolver;

    public TimedSecureConnectionSocketFactory(@NotNull LayeredConnectionSocketFactory delegate, @NotNull DnsResolver dnsResolver) {
        this.delegate = delegate;
        this.dnsResolver = dnsResolver;
    }

    @Override
    public Socket createSocket(@NotNull HttpContext context) throws IOException {
        return this.delegate.createSocket(context);
    }

    @Override
    public Socket connectSocket(int connectTimeout,
                                @NotNull Socket socket,
                                @NotNull HttpHost host,
                                @NotNull InetSocketAddress remoteAddress,
                                InetSocketAddress localAddress,
                                @NotNull HttpContext context) throws IOException {
        // Time DNS resolution
        long dnsStart = System.nanoTime();
        InetAddress[] addresses = dnsResolver.resolve(host.getHostName());
        long dnsTime = System.nanoTime() - dnsStart;
        context.setAttribute(Latency.DNS_TIME, dnsTime);

        // Time TCP + TLS connection (combined in initial connect for HTTPS)
        long connectStart = System.nanoTime();
        Socket result = delegate.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
        long connectTime = System.nanoTime() - connectStart;

        // For HTTPS, this includes both TCP and TLS handshake
        context.setAttribute(Latency.TCP_CONNECT_TIME, connectTime);

        // Extract TLS info
        if (result instanceof SSLSocket) {
            SSLSession session = ((SSLSocket) result).getSession();
            context.setAttribute(Latency.TLS_PROTOCOL, session.getProtocol());
            context.setAttribute(Latency.TLS_CIPHER, session.getCipherSuite());
        }

        return result;
    }

    @Override
    public Socket createLayeredSocket(@NotNull Socket socket,
                                      @NotNull String target,
                                      int port,
                                      @NotNull HttpContext context) throws IOException {
        // Time TLS handshake specifically (when layering TLS over existing connection)
        long tlsStart = System.nanoTime();
        Socket result = delegate.createLayeredSocket(socket, target, port, context);
        long tlsTime = System.nanoTime() - tlsStart;
        context.setAttribute(Latency.TLS_HANDSHAKE_TIME, tlsTime);

        // Extract TLS info
        if (result instanceof SSLSocket) {
            SSLSession session = ((SSLSocket) result).getSession();
            context.setAttribute(Latency.TLS_PROTOCOL, session.getProtocol());
            context.setAttribute(Latency.TLS_CIPHER, session.getCipherSuite());
        }

        return result;
    }
}