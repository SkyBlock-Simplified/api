package gg.sbs.api.apiclients.hypixel;

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface HypixelPlayerDataApi {
    @RequestLine("GET /guild")
    HypixelGuildResponse getGuild(@HeaderMap Map<String, String> headerMap, @Param("player") String uuid);

    @RequestLine("GET /player")
    HypixelPlayerResponse getPlayer(@HeaderMap Map<String, String> headerMap, @Param("uuid") String uuid);
}