package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.client.hypixel.response.hypixel.HypixelCountsResponse;
import dev.sbs.api.client.hypixel.response.hypixel.HypixelGuildResponse;
import dev.sbs.api.client.hypixel.response.hypixel.HypixelPlayerResponse;
import dev.sbs.api.client.hypixel.response.hypixel.HypixelPunishmentStatsResponse;
import dev.sbs.api.client.hypixel.response.hypixel.HypixelStatusResponse;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface HypixelPlayerRequest extends IHypixelRequest {

    @RequestLine("GET /counts")
    @NotNull HypixelCountsResponse getCounts();

    @RequestLine("GET /guild?id={id}")
    @NotNull HypixelGuildResponse getGuild(@Param("id") String guildId);

    @RequestLine("GET /guild?name={name}")
    @NotNull HypixelGuildResponse getGuildByName(@Param("name") String guildName);

    @RequestLine("GET /guild?player={player}")
    @NotNull HypixelGuildResponse getGuildByPlayer(@Param("player") UUID playerId);

    @RequestLine("GET /player?uuid={uuid}")
    @NotNull HypixelPlayerResponse getPlayer(@Param("uuid") UUID playerId);

    @RequestLine("GET /punishmentstats")
    @NotNull HypixelPunishmentStatsResponse getPunishmentStats();

    @RequestLine("GET /status?uuid={uuid}")
    @NotNull HypixelStatusResponse getStatus(@Param("uuid") UUID playerId);

}
