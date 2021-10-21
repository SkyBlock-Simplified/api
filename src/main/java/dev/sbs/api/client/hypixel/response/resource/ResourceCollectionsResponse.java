package dev.sbs.api.client.hypixel.response.resource;

import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceCollectionsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private String version;
    @Getter ConcurrentMap<String, Collection> collections;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Collection {

        @Getter private String name;
        @Getter private ConcurrentMap<String, CollectionItem> items;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CollectionItem {

        @Getter private String name;
        @Getter private int maxTiers;
        @Getter private ConcurrentList<CollectionTier> tiers;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CollectionTier {

        @Getter private int tier;
        @Getter private int amountRequired;
        @Getter private ConcurrentList<String> unlocks;
    }

}
