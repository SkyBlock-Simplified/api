package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionsEndedResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockAuctionsResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockBazaarResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockFireSaleResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockMuseumResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockNewsResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockProfileResponse;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockProfilesResponse;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface HypixelSkyBlockRequest extends HypixelRequestInterface {

    @RequestLine("GET /skyblock/museum?profile={profile}")
    SkyBlockMuseumResponse getMuseum();

    @RequestLine("GET /skyblock/news")
    SkyBlockNewsResponse getNews();

    @Deprecated
    @RequestLine("GET /skyblock/profile?profile={profile}")
    SkyBlockProfileResponse getProfile(@Param("profile") UUID uniqueId);

    @RequestLine("GET /skyblock/profiles?uuid={uuid}")
    SkyBlockProfilesResponse getProfiles(@Param("uuid") UUID uniqueId);

    @RequestLine("GET /skyblock/bazaar")
    SkyBlockBazaarResponse getBazaar();

    @RequestLine("GET /skyblock/auction?uuid={uuid}")
    SkyBlockAuctionResponse getAuction(@Param("uuid") UUID auctionId);

    @RequestLine("GET /skyblock/auction?profile={profile}")
    SkyBlockAuctionResponse getAuctionByIsland(@Param("profile") UUID islandId);

    @RequestLine("GET /skyblock/auction?player={player}")
    SkyBlockAuctionResponse getAuctionByPlayer(@Param("player") UUID playerId);

    @RequestLine("GET /skyblock/auctions")
    SkyBlockAuctionsResponse getAuctions();

    @RequestLine("GET /skyblock/auctions?page={page}")
    SkyBlockAuctionsResponse getAuctions(@Param("page") Integer page);

    @RequestLine("GET /skyblock/auctions_ended")
    SkyBlockAuctionsEndedResponse getEndedAuctions();

    @RequestLine("GET /skyblock/firesales")
    SkyBlockFireSaleResponse getFireSales();

}
