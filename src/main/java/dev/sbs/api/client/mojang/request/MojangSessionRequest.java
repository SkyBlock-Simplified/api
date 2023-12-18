package dev.sbs.api.client.mojang.request;

import dev.sbs.api.client.mojang.exception.MojangApiException;
import dev.sbs.api.client.mojang.response.MojangPropertiesResponse;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MojangSessionRequest extends IMojangRequest {

    @RequestLine("GET /session/minecraft/profile/{uniqueId}")
    @NotNull MojangPropertiesResponse getProperties(@NotNull @Param("uniqueId") UUID uniqueId) throws MojangApiException;

}
