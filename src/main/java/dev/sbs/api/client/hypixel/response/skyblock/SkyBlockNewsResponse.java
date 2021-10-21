package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockNewsResponse {

    @Getter private boolean success;
    @SerializedName("items")
    @Getter private ConcurrentList<Article> articles;

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
                throw new IllegalArgumentException(FormatUtil.format("Unable to create URL ''{0}''", this.link));
            }
        }

    }

}
