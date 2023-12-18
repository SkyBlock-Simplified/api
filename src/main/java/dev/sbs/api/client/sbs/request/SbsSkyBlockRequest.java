package dev.sbs.api.client.sbs.request;

import dev.sbs.api.client.sbs.response.SkyBlockEmojis;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.client.sbs.response.SkyBlockImagesResponse;
import dev.sbs.api.client.sbs.response.SkyBlockItemsResponse;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

public interface SbsSkyBlockRequest extends ISbsRequest {

    @RequestLine("GET /skyblock/emojis.json")
    @NotNull SkyBlockEmojisResponse getEmojis();

    @RequestLine("GET /skyblock/images.json")
    @NotNull SkyBlockImagesResponse getImages();

    @RequestLine("GET /skyblock/items.json")
    @NotNull SkyBlockItemsResponse getItems();

    default @NotNull SkyBlockEmojis getItemEmojis() {
        return new SkyBlockEmojis(
            this.getItems(),
            this.getEmojis(),
            this.getImages()
        );
    }

}
