package dev.sbs.api.client.sbs.response;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockEmojisResponse {

    private ConcurrentMap<String, ConcurrentMap<String, Emoji>> emojis = Concurrent.newMap();

    public Optional<Emoji> getEmoji(String itemId) {
        return this.getEmoji(itemId, false);
    }

    public Optional<Emoji> getEmoji(String itemId, boolean enchanted) {
        return Optional.ofNullable(this.emojis.getOrDefault(itemId, null)).map(itemMap -> {
            Emoji normalEmoji = itemMap.get("normal");

            if (enchanted) {
                Emoji enchantedEmoji = itemMap.get("enchanted");
                return enchantedEmoji.isEnchanted() ? enchantedEmoji : normalEmoji;
            }

            return normalEmoji;
        });
    }

    public Optional<Emoji> getPetEmoji(String petId) {
        return this.getEmoji(petId + "_PET");
    }

    public static class Emoji {

        @Getter private String name;
        @Getter private long id;
        @SerializedName("formatted")
        @Getter private String format;
        @Getter private boolean animated;
        @Getter private boolean enchanted;

    }

}
