package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Collection {

    private final CollectionModel type;
    @SerializedName("items")
    ConcurrentLinkedMap<CollectionItemModel, Long> collected = Concurrent.newLinkedMap();
    ConcurrentLinkedMap<CollectionItemModel, Integer> unlocked = Concurrent.newLinkedMap();

    public long getCollected(CollectionItemModel collection) {
        return this.collected.get(collection);
    }

    public int getUnlocked(CollectionItemModel collection) {
        return this.unlocked.getOrDefault(collection, 0);
    }

}
