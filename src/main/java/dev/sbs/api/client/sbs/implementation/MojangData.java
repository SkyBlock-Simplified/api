package dev.sbs.api.client.sbs.implementation;

import dev.sbs.api.client.sbs.SbsRequestInterface;
import dev.sbs.api.client.sbs.response.MojangProfileResponse;
import dev.sbs.api.client.sbs.response.MojangStatusResponse;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface MojangData extends SbsRequestInterface {

    @RequestLine("GET /mojang/user/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /mojang/user/{uniqueId}")
    MojangProfileResponse getProfileFromUniqueId(@Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang/status")
    MojangStatusResponse getStatus();

}
