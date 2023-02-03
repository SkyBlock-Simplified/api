package dev.sbs.api.client.sbs.request;

import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import feign.RequestLine;

public interface SkyBlockRequest extends SbsRequestInterface {

    @RequestLine("GET /skyblock/emojis.json")
    SkyBlockEmojisResponse getEmojis();

}
