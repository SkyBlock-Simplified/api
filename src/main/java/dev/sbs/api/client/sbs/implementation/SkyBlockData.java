package dev.sbs.api.client.sbs.implementation;

import dev.sbs.api.client.sbs.SbsRequestInterface;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import feign.RequestLine;

public interface SkyBlockData extends SbsRequestInterface {

    @RequestLine("GET /skyblock/emojis.json")
    SkyBlockEmojisResponse getEmojis();

}
