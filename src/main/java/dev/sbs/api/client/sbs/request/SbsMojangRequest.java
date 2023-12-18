package dev.sbs.api.client.sbs.request;

import dev.sbs.api.client.sbs.response.MojangProfileResponse;
import dev.sbs.api.client.sbs.response.MojangStatusResponse;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SbsMojangRequest extends ISbsRequest {

    @RequestLine("GET /mojang/user/{username}")
    @NotNull MojangProfileResponse getProfileFromUsername(@NotNull @Param("username") String username);

    @RequestLine("GET /mojang/user/{uniqueId}")
    @NotNull MojangProfileResponse getProfileFromUniqueId(@NotNull @Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang/status")
    @NotNull MojangStatusResponse getStatus();

}
