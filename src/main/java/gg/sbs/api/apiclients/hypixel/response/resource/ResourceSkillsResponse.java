package gg.sbs.api.apiclients.hypixel.response.resource;

import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import lombok.Getter;

public class ResourceSkillsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private String version;
    @Getter private ConcurrentMap<String, Skill> collections;
    @Getter private ConcurrentMap<String, Skill> skills;

    public static class Skill {

        @Getter private String name;
        @Getter private String description;
        @Getter private int maxLevel;
        @Getter private ConcurrentList<SkillLevel> levels;

    }

    public static class SkillLevel {

        @Getter private int level;
        @Getter private double totalExpRequired;
        @Getter private ConcurrentList<String> unlocks;

    }

}