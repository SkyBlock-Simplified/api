package dev.sbs.api.client.impl.mojang.client;

import dev.sbs.api.client.impl.mojang.request.MojangSessionRequest;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;

public class MojangSessionClient extends MojangClient<MojangSessionRequest> {

    public MojangSessionClient() {
        this(null);
    }

    public MojangSessionClient(@Nullable Inet6Address inet6Address) {
        super(Domain.MOJANG_SESSIONSERVER, inet6Address);
    }

}