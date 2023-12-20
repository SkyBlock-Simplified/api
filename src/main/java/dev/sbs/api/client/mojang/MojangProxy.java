package dev.sbs.api.client.mojang;

import dev.sbs.api.client.mojang.client.MojangApiClient;
import dev.sbs.api.client.mojang.client.MojangClient;
import dev.sbs.api.client.mojang.client.MojangSessionClient;
import dev.sbs.api.client.mojang.exception.MojangApiException;
import dev.sbs.api.client.mojang.request.MojangApiRequest;
import dev.sbs.api.client.mojang.request.MojangSessionRequest;
import dev.sbs.api.client.sbs.response.MojangProfileResponse;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.PrimitiveUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class MojangProxy {

    // API: http://wiki.vg/Mojang_API

    // Skin Renderer
    // https://visage.surgeplay.com/index.html
    // https://github.com/unascribed-archive/Visage

    private final @NotNull MojangApiClient defaultApiClient;
    private final @NotNull MojangSessionClient defaultSessionClient;
    private final @NotNull ConcurrentList<MojangApiClient> inet6ApiClients = Concurrent.newList();
    private final @NotNull ConcurrentList<MojangSessionClient> ipv6SessionClients = Concurrent.newList();
    @Getter private @NotNull Optional<Integer[]> inet6NetworkPrefix = Optional.empty();

    public MojangProxy() {
        this.defaultApiClient = new MojangApiClient();
        this.defaultSessionClient = new MojangSessionClient();
    }

    private @NotNull MojangApiClient createInet6ApiClient() {
        return new MojangApiClient(this.getRandomInet6Address());
    }

    private @NotNull MojangSessionClient createInet6SessionClient() {
        return new MojangSessionClient(this.getRandomInet6Address());
    }

    public @NotNull MojangApiClient getApiClient() {
        if (this.defaultApiClient.notRateLimited())
            return this.defaultApiClient;

        if (this.getInet6NetworkPrefix().isPresent()) {
            return this.inet6ApiClients.stream()
                .filter(MojangClient::notRateLimited)
                .findFirst()
                .orElse(this.createInet6ApiClient());
        }

        return this.defaultApiClient;
    }

    public @NotNull MojangApiRequest getApiRequest() {
        return this.getApiClient().getRequest();
    }

    /**
     * Gets the {@link MojangProfileResponse} for the given username.
     *
     * @param username Unique profile username (case-insensitive).
     */
    public @NotNull MojangProfileResponse getMojangProfile(@NotNull String username) throws MojangApiException {
        return getMojangProfile(this.getApiRequest().getUniqueId(username).getUniqueId());
    }

    /**
     * Gets the {@link MojangProfileResponse} for the given unique id.
     *
     * @param uniqueId Unique profile identifier.
     */
    public @NotNull MojangProfileResponse getMojangProfile(@NotNull UUID uniqueId) throws MojangApiException {
        return new MojangProfileResponse(this.getSessionRequest().getProperties(uniqueId));
    }

    private @NotNull Inet6Address getRandomInet6Address() {
        return this.getInet6NetworkPrefix()
            .map(networkPrefix -> {
                String inet6NetworkPrefix = StringUtil.join(networkPrefix, ":");

                try {
                    return Inet6Address.getByName(String.format(
                        "%s:%04x:%04x:%04x:%04x",
                        inet6NetworkPrefix,
                        getRandomInet6Group(),
                        getRandomInet6Group(),
                        getRandomInet6Group(),
                        getRandomInet6Group()
                    ));
                } catch (UnknownHostException uhex) {
                    throw SimplifiedException.wrapNative(uhex).build();
                }
            })
            .map(Inet6Address.class::cast)
            .orElseThrow();
    }

    private static int getRandomInet6Group() {
        return ThreadLocalRandom.current().nextInt() & 0xFFFF;
    }

    public @NotNull MojangSessionClient getSessionClient() {
        if (this.defaultSessionClient.notRateLimited())
            return this.defaultSessionClient;

        if (this.getInet6NetworkPrefix().isPresent()) {
            return this.ipv6SessionClients.stream()
                .filter(MojangClient::notRateLimited)
                .findFirst()
                .orElse(this.createInet6SessionClient());
        }

        return this.defaultSessionClient;
    }

    public @NotNull MojangSessionRequest getSessionRequest() {
        return this.getSessionClient().getRequest();
    }

    /**
     * Set your assigned IPv6 network prefix to cycle through for web requests.
     * <br><br>
     * You must run commands as root on your server to make this possible:
     * <pre><code>
     * ip route add local {ipv6address}/{prefix} dev {interface}
     * sysctl net.ipv6.ip_nonlocal_bind = 1
     * </code></pre>
     * You must also edit <code>/etc/sysctl.d/{interface}</code>
     *
     * @param networkPrefix Your IPv6 Network Prefix
     */
    public void setInet6NetworkPrefix(int[] networkPrefix) {
        this.inet6NetworkPrefix = Optional.ofNullable(PrimitiveUtil.wrap(networkPrefix));
    }

}
