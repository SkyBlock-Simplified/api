package dev.sbs.api.client.impl.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockArticle;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyBlockNewsResponse {

    private boolean success;
    @SerializedName("items")
    private @NotNull ConcurrentList<SkyBlockArticle> articles = Concurrent.newList();

}
