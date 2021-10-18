package gg.sbs.api.apiclients.mojang.implementation;

import feign.Param;
import feign.RequestLine;
import gg.sbs.api.apiclients.mojang.response.MojangProfileResponse;
import gg.sbs.api.apiclients.mojang.response.MojangStatusResponse;

import java.util.UUID;

public interface MojangData extends MojangRequestInterface {

    @RequestLine("GET /mojang2/user/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /mojang2/user/{uniqueId}")
    MojangProfileResponse getProfileFromUniqueId(@Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang2/status")
    MojangStatusResponse getStatus();

}