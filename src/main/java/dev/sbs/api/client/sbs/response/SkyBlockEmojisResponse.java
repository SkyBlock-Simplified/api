package dev.sbs.api.client.sbs.response;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
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
        private String id;
        @SerializedName("formatted")
        @Getter private String format;
        @Getter private boolean animated;
        @Getter private boolean enchanted;

        public long getId() {
            return Long.parseLong(this.id);
        }

    }

    public static class Deserializer implements JsonDeserializer<SkyBlockEmojisResponse> {

        @Override
        public SkyBlockEmojisResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            Gson gson = SimplifiedApi.getGson();
            SkyBlockEmojisResponse skyBlockEmojisResponse = new SkyBlockEmojisResponse();

            jsonElement.getAsJsonObject()
                .entrySet()
                .forEach(entry -> {
                    skyBlockEmojisResponse.emojis.put(entry.getKey(), Concurrent.newMap());

                    entry.getValue()
                        .getAsJsonObject()
                        .entrySet()
                        .forEach(subEntry -> skyBlockEmojisResponse.emojis.get(entry.getKey())
                            .put(subEntry.getKey(), gson.fromJson(subEntry.getValue(), Emoji.class))
                        );
                });

            return skyBlockEmojisResponse;
        }

    }

}
