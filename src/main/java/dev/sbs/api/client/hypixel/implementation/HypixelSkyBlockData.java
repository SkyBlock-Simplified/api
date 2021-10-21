package dev.sbs.api.client.hypixel.implementation;

import dev.sbs.api.client.hypixel.response.skyblock.*;
import feign.Param;
import feign.RequestLine;

import java.util.UUID;

public interface HypixelSkyBlockData extends HypixelRequestInterface {

    @RequestLine("GET /skyblock/news")
    SkyBlockNewsResponse getNews();

    @Deprecated
    @RequestLine("GET /skyblock/profile?profile={profile}")
    SkyBlockProfileResponse getProfile(@Param("profile") UUID uniqueId);

    @RequestLine("GET /skyblock/profiles?uuid={uuid}")
    SkyBlockProfilesResponse getProfiles(@Param("uuid") UUID uniqueId);

    @RequestLine("GET /skyblock/bazaar")
    SkyBlockBazaarResponse getBazaar();

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuction(@Param("uuid") UUID auctionId);

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuctionByIsland(@Param("profile") UUID islandId);

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuctionByPlayer(@Param("player") UUID playerId);

    @RequestLine("GET /skyblock/auctions")
    SkyBlockAuctionsResponse getAuctions();

    @RequestLine("GET /skyblock/auctions?page={page}")
    SkyBlockAuctionsResponse getAuctions(@Param("page") Integer page);

    @RequestLine("GET /skyblock/auctions_ended")
    SkyBlockAuctionsEndedResponse getEndedAuctions();

}
