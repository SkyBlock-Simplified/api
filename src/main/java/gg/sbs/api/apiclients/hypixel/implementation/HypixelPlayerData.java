package gg.sbs.api.apiclients.hypixel.implementation;

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;
import gg.sbs.api.apiclients.hypixel.response.hypixel.*;

import java.util.Map;
import java.util.UUID;

public interface HypixelPlayerData extends HypixelDataInterface {

    @RequestLine("GET /counts")
    HypixelCountsResponse getCounts(@HeaderMap Map<String, String> headerMap);

    @RequestLine("GET /friends")
    HypixelFriendsResponse getFriends(@HeaderMap Map<String, String> headerMap, @Param("uuid") UUID uniqueId);

    @RequestLine("GET /guild")
    HypixelGuildResponse getGuild(@HeaderMap Map<String, String> headerMap, @Param("name") String name);

    @RequestLine("GET /guild")
    HypixelGuildResponse getGuild(@HeaderMap Map<String, String> headerMap, @Param("player") UUID uniqueId);

    @RequestLine("GET /player")
    HypixelPlayerResponse getPlayer(@HeaderMap Map<String, String> headerMap, @Param("uuid") UUID uniqueId);

    @RequestLine("GET /punishmentstats")
    HypixelPunishmentStatsResponse getPunishmentStats(@HeaderMap Map<String, String> headerMap);

    @RequestLine("GET /status")
    HypixelStatusResponse getStatus(@HeaderMap Map<String, String> headerMap, @Param("uuid") UUID uniqueId);

}