package dev.sbs.api.client.hypixel.implementation;

import dev.sbs.api.client.hypixel.response.hypixel.*;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface HypixelPlayerData extends HypixelRequestInterface {

    @RequestLine("GET /counts")
    HypixelCountsResponse getCounts();

    @RequestLine("GET /friends?uuid={uuid}")
    HypixelFriendsResponse getFriends(@Param("uuid") UUID uniqueId);

    @RequestLine("GET /guild?id={id}")
    HypixelGuildResponse getGuild(@Param("id") String guildId);

    @RequestLine("GET /guild?name={name}")
    HypixelGuildResponse getGuildByName(@Param("name") String guildName);

    @RequestLine("GET /guild?player={player}")
    HypixelGuildResponse getGuildByPlayer(@Param("player") UUID uniqueId);

    @RequestLine("GET /player?uuid={uuid}")
    HypixelPlayerResponse getPlayer(@Param("uuid") UUID uniqueId);

    @RequestLine("GET /punishmentstats")
    HypixelPunishmentStatsResponse getPunishmentStats();

    @RequestLine("GET /status?uuid={uuid}")
    HypixelStatusResponse getStatus(@Param("uuid") UUID uniqueId);

}