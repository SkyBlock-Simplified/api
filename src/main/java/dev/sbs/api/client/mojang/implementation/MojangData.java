package dev.sbs.api.client.mojang.implementation;

import dev.sbs.api.client.mojang.response.MojangProfileResponse;
import dev.sbs.api.client.mojang.response.MojangStatusResponse;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface MojangData extends MojangRequestInterface {

    @RequestLine("GET /mojang/user/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /mojang/user/{uniqueId}")
    MojangProfileResponse getProfileFromUniqueId(@Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang/status")
    MojangStatusResponse getStatus();

}
