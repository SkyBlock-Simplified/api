package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionsEndedResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionsResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockBazaarResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockFireSaleResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockMuseumResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockNewsResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockProfilesResponse;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface HypixelSkyBlockRequest extends IHypixelRequest {

    @RequestLine("GET /skyblock/museum?profile={profile}")
    @NotNull SkyBlockMuseumResponse getMuseum();

    @RequestLine("GET /skyblock/news")
    @NotNull SkyBlockNewsResponse getNews();

    @RequestLine("GET /v2/skyblock/profiles?uuid={uuid}")
    @NotNull SkyBlockProfilesResponse getProfiles(@Param("uuid") UUID uniqueId);

    @RequestLine("GET /skyblock/bazaar")
    @NotNull SkyBlockBazaarResponse getBazaar();

    @RequestLine("GET /skyblock/auction?uuid={uuid}")
    @NotNull SkyBlockAuctionResponse getAuction(@Param("uuid") UUID auctionId);

    @RequestLine("GET /skyblock/auction?profile={profile}")
    @NotNull SkyBlockAuctionResponse getAuctionByIsland(@Param("profile") UUID islandId);

    @RequestLine("GET /skyblock/auction?player={player}")
    @NotNull SkyBlockAuctionResponse getAuctionByPlayer(@Param("player") UUID playerId);

    @RequestLine("GET /skyblock/auctions")
    @NotNull SkyBlockAuctionsResponse getAuctions();

    @RequestLine("GET /skyblock/auctions?page={page}")
    @NotNull SkyBlockAuctionsResponse getAuctions(@Param("page") Integer page);

    @RequestLine("GET /skyblock/auctions_ended")
    @NotNull SkyBlockAuctionsEndedResponse getEndedAuctions();

    @RequestLine("GET /skyblock/firesales")
    @NotNull SkyBlockFireSaleResponse getFireSales();

}
