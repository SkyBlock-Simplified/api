package dev.sbs.api.client.timing;

import org.apache.http.HttpHost;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class TimedPlainConnectionSocketFactory implements ConnectionSocketFactory {

    private final @NotNull ConnectionSocketFactory delegate;
    private final @NotNull DnsResolver dnsResolver;

    public TimedPlainConnectionSocketFactory(@NotNull ConnectionSocketFactory delegate, @NotNull DnsResolver dnsResolver) {
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
                                @Nullable InetSocketAddress localAddress,
                                @NotNull HttpContext context) throws IOException {
        // Time DNS resolution
        long dnsStart = System.nanoTime();
        InetAddress[] addresses = this.dnsResolver.resolve(host.getHostName());
        long dnsTime = System.nanoTime() - dnsStart;
        context.setAttribute(Latency.DNS_TIME, dnsTime);

        // Time TCP connection
        long connectStart = System.nanoTime();
        Socket result = delegate.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
        long connectTime = System.nanoTime() - connectStart;
        context.setAttribute(Latency.TCP_CONNECT_TIME, connectTime);

        return result;
    }
}