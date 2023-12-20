package dev.sbs.api.client.impl.sbs.response;

import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class SkyBlockEmojis {

    private final SkyBlockItemsResponse items;
    private final SkyBlockEmojisResponse emojis;
    private final SkyBlockImagesResponse images;

    public ConcurrentMap<String, ConcurrentMap<Boolean, SkyBlockEmojisResponse.Emoji>> getEmojis() {
        return this.emojis.getItems();
    }

    public ConcurrentMap<String, SkyBlockImagesResponse.Image> getImages() {
        return this.images.getItems();
    }

    public ConcurrentMap<String, String> getItems() {
        return this.items.getItems();
    }


    public Optional<SkyBlockEmojisResponse.Emoji> getEmoji(@NotNull String itemId) {
        return this.getEmoji(itemId, false);
    }

    public Optional<SkyBlockEmojisResponse.Emoji> getEmoji(@NotNull String itemId, boolean enchanted) {
        return this.items.getItemId(itemId)
            .flatMap(id -> this.emojis.getEmoji(id, enchanted))
            .map(emoji -> new SkyBlockEmojisResponse.Emoji(itemId, emoji));
    }

    public Optional<SkyBlockEmojisResponse.Emoji> getPetEmoji(@NotNull String petId) {
        return this.getEmoji("PET_" + petId);
    }

    public Optional<SkyBlockEmojisResponse.Emoji> getRuneEmoji(@NotNull String runeId) {
        return this.getEmoji("RUNE_" + runeId);
    }
}
