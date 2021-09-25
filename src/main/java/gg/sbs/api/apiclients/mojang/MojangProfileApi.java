package gg.sbs.api.apiclients.mojang;

import feign.Param;
import feign.RequestLine;

public interface MojangProfileApi {
    @RequestLine("GET /mojang/v2/user/{uuidOrUsername}")
    MojangProfileResponse getProfileFromUsername(@Param("uuidOrUsername") String uuidOrUsername);
}
