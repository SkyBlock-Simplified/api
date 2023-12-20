package dev.sbs.api.client.impl.mojang.request;

import dev.sbs.api.client.impl.mojang.exception.MojangApiException;
import dev.sbs.api.client.impl.mojang.response.MojangPropertiesResponse;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MojangSessionRequest extends IMojangRequest {

    @RequestLine("GET /session/minecraft/profile/{uniqueId}?unsigned=false")
    @NotNull MojangPropertiesResponse getProperties(@NotNull @Param("uniqueId") UUID uniqueId) throws MojangApiException;

}
