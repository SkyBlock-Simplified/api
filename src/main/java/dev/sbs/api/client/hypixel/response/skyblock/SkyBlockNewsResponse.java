package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockNewsResponse {

    private boolean success;
    @SerializedName("items")
    private ConcurrentList<Article> articles;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Article {

        @SerializedName("item.material")
        @Getter private String material;
        private String link;
        @SerializedName("text")
        @Getter private String date;
        @Getter private String title;

        public URL getUrl() {
            try {
                return new URL(this.link);
            } catch (MalformedURLException muex) {
                throw new IllegalArgumentException(String.format("Unable to create URL '%s'!", this.link));
            }
        }

    }

}
