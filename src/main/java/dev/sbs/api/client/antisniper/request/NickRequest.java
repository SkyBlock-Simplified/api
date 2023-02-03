package dev.sbs.api.client.antisniper.request;

import dev.sbs.api.client.antisniper.response.DeNickResponse;
import dev.sbs.api.client.antisniper.response.FindNickResponse;
import feign.Param;
import feign.RequestLine;

public interface NickRequest extends AntiSniperRequestInterface {

    @RequestLine("GET /denick?nick={nickname}")
    DeNickResponse getUserFromNickname(@Param("nickname") String nickname);

    @RequestLine("GET /findnick?name={username}")
    FindNickResponse getNicknameFromUser(@Param("username") String username);

}
