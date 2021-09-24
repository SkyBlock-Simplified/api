package gg.sbs.api.httpclients.mojang;

import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface MojangProfileApi {
    @RequestLine("GET /users/profiles/minecraft/{username}")
    MojangProfileResponse getProfileFromUsername(@Param("username") String username);

    @RequestLine("GET /user/profiles/{uuid}/names")
    List<MojangProfileChange> getNameHistory(@Param("uuid") String uuid);
}
