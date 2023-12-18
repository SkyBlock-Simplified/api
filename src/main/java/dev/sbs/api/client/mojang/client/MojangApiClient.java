package dev.sbs.api.client.mojang.client;

import dev.sbs.api.client.mojang.request.MojangApiRequest;
import org.jetbrains.annotations.Nullable;

import java.net.Inet6Address;

public class MojangApiClient extends MojangClient<MojangApiRequest> {

    public MojangApiClient() {
        this(null);
    }

    public MojangApiClient(@Nullable Inet6Address inet6Address) {
        super(Domain.MOJANG_API, inet6Address);
    }

}