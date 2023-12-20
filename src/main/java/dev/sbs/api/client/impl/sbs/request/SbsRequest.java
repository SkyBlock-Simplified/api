package dev.sbs.api.client.impl.sbs.request;

import dev.sbs.api.client.impl.sbs.response.MojangProfileResponse;
import dev.sbs.api.client.impl.sbs.response.MojangStatusResponse;
import dev.sbs.api.client.impl.sbs.response.SkyBlockEmojis;
import dev.sbs.api.client.impl.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.client.impl.sbs.response.SkyBlockImagesResponse;
import dev.sbs.api.client.impl.sbs.response.SkyBlockItemsResponse;
import dev.sbs.api.client.request.IRequest;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SbsRequest extends IRequest {

    @RequestLine("GET /test/{username}")
    @Deprecated
    @NotNull MojangProfileResponse getTestProfileFromUsername(@NotNull @Param("username") String username);

    @RequestLine("GET /mojang/user/{username}")
    @NotNull MojangProfileResponse getProfileFromUsername(@NotNull @Param("username") String username);

    @RequestLine("GET /mojang/user/{uniqueId}")
    @NotNull MojangProfileResponse getProfileFromUniqueId(@NotNull @Param("uniqueId") UUID uniqueId);

    @RequestLine("GET /mojang/status")
    @NotNull MojangStatusResponse getStatus();

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
