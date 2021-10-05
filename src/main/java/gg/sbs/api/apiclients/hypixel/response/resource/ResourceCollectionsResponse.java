package gg.sbs.api.apiclients.hypixel.response.resource;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class ResourceCollectionsResponse {
    @Getter
    private boolean success;

    @Getter
    private long lastUpdated;

    @Getter
    private String version;

    @Getter
    Map<String, Collection> collections;

    public static class Collection {
        @Getter
        private String name;

        @Getter
        private Map<String, CollectionItem> items;
    }

    public static class CollectionItem {
        @Getter
        private String name;

        @Getter
        private int maxTiers;

        @Getter
        private List<CollectionTier> tiers;
    }

    public static class CollectionTier {
        @Getter
        private int tier;

        @Getter
        private int amountRequired;

        @Getter
        private List<String> unlocks;
    }
}
