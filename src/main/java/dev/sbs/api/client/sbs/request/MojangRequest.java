package dev.sbs.api.client.sbs.request;

import dev.sbs.api.client.sbs.response.MojangProfileResponse;
import dev.sbs.api.client.sbs.response.MojangStatusResponse;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface MojangRequest extends SbsRequest {

    @RequestLine("GET /mojang/user/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /mojang/user/{uniqueId}")
    MojangProfileResponse getProfileFromUniqueId(@Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang/status")
    MojangStatusResponse getStatus();

}
