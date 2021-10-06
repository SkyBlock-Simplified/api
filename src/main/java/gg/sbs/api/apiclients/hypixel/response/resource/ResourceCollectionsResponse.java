package gg.sbs.api.apiclients.hypixel.response.resource;

import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import lombok.Getter;

public class ResourceCollectionsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private String version;
    @Getter ConcurrentMap<String, Collection> collections;

    public static class Collection {

        @Getter private String name;
        @Getter private ConcurrentMap<String, CollectionItem> items;

    }

    public static class CollectionItem {

        @Getter private String name;
        @Getter private int maxTiers;
        @Getter private ConcurrentList<CollectionTier> tiers;

    }

    public static class CollectionTier {

        @Getter private int tier;
        @Getter private int amountRequired;
        @Getter private ConcurrentList<String> unlocks;
    }

}