package dev.sbs.api.client.antisniper.request;

import dev.sbs.api.client.antisniper.response.DenickResponse;
import dev.sbs.api.client.antisniper.response.FindnickResponse;
import feign.Param;
import feign.RequestLine;

public interface DenickRequest extends AntiSniperRequestInterface {

    @RequestLine("GET /denick?nick={nickname}")
    DenickResponse getUserFromNickname(@Param("nickname") String nickname);

    @RequestLine("GET /findnick?name={username}")
    FindnickResponse getNicknameFromUser(@Param("username") String username);

}
