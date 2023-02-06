package dev.sbs.api.client.sbs.request;

import dev.sbs.api.client.sbs.response.SkyBlockEmojis;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.client.sbs.response.SkyBlockImagesResponse;
import dev.sbs.api.client.sbs.response.SkyBlockItemsResponse;
import feign.RequestLine;

public interface SkyBlockRequest extends SbsRequestInterface {

    @RequestLine("GET /skyblock/emojis.json")
    SkyBlockEmojisResponse getEmojis();

    @RequestLine("GET /skyblock/images.json")
    SkyBlockImagesResponse getImages();

    @RequestLine("GET /skyblock/items.json")
    SkyBlockItemsResponse getItems();

    default SkyBlockEmojis getItemEmojis() {
        return new SkyBlockEmojis(
            this.getItems(),
            this.getEmojis(),
            this.getImages()
        );
    }

}
