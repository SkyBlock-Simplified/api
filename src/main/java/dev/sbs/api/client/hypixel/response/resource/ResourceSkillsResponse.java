package dev.sbs.api.client.hypixel.response.resource;

import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceSkillsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private String version;
    @Getter private ConcurrentMap<String, Skill> collections;
    @Getter private ConcurrentMap<String, Skill> skills;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Skill {

        @Getter private String name;
        @Getter private String description;
        @Getter private int maxLevel;
        @Getter private ConcurrentList<SkillLevel> levels;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SkillLevel {

        @Getter private int level;
        @Getter private double totalExpRequired;
        @Getter private ConcurrentList<String> unlocks;

    }

}