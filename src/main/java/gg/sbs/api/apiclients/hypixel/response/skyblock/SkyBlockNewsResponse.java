package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;

public class SkyBlockNewsResponse {

    @Getter private boolean success;
    @SerializedName("items")
    @Getter private ConcurrentList<Article> articles;

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