package gg.sbs.api.apiclients.hypixel.implementation;

import feign.HeaderMap;
import feign.Param;
import feign.RequestLine;
import gg.sbs.api.apiclients.hypixel.response.SkyBlockNewsResponse;
import gg.sbs.api.apiclients.hypixel.response.SkyBlockProfileResponse;
import gg.sbs.api.apiclients.hypixel.response.SkyBlockProfilesResponse;

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

    /*
		AUCTION("skyblock/auction", "auctions")
		AUCTIONS("skyblock/auctions", "auctions")
		BANK("skyblock/bank", "bank")
     */

}