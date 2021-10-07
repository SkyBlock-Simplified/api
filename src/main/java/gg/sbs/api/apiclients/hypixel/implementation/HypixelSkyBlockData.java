package gg.sbs.api.apiclients.hypixel.implementation;

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;
import gg.sbs.api.apiclients.hypixel.response.skyblock.*;

import java.util.Map;
import java.util.UUID;

public interface HypixelSkyBlockData extends HypixelDataInterface {

    @RequestLine("GET /skyblock/news")
    SkyBlockNewsResponse getNews(@HeaderMap Map<String, String> headerMap);

    @Deprecated
    @RequestLine("GET /skyblock/profile")
    SkyBlockProfileResponse getProfile(@HeaderMap Map<String, String> headerMap, @Param("profile") UUID uniqueId);

    @RequestLine("GET /skyblock/profiles")
    SkyBlockProfilesResponse getProfiles(@HeaderMap Map<String, String> headerMap, @Param("uuid") UUID uniqueId);

    @RequestLine("GET /skyblock/bazaar")
    SkyBlockBazaarResponse getBazaar();

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuction(@HeaderMap Map<String, String> headerMap, @Param("uuid") UUID auctionId);

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuctionByIsland(@HeaderMap Map<String, String> headerMap, @Param("profile") UUID islandId);

    @RequestLine("GET /skyblock/auction")
    SkyBlockAuctionResponse getAuctionByPlayer(@HeaderMap Map<String, String> headerMap, @Param("player") UUID playerId);

    @RequestLine("GET /skyblock/auctions")
    SkyBlockAuctionsResponse getAuctions();

    @RequestLine("GET /skyblock/auctions")
    SkyBlockAuctionsResponse getAuctions(@Param("page") Integer page);

    @RequestLine("GET /skyblock/auctions_ended")
    SkyBlockAuctionsEndedResponse getEndedAuctions();

}