package dev.sbs.api.client.mojang.implementation;

import feign.Param;
import feign.RequestLine;
import dev.sbs.api.client.mojang.response.MojangProfileResponse;
import dev.sbs.api.client.mojang.response.MojangStatusResponse;

import java.util.UUID;

public interface MojangData extends MojangRequestInterface {

    @RequestLine("GET /mojang2/user/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /mojang2/user/{uniqueId}")
    MojangProfileResponse getProfileFromUniqueId(@Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang2/status")
    MojangStatusResponse getStatus();

}