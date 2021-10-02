package gg.sbs.api.apiclients.hypixel.response;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class ResourceSkillsResponse {
    @Getter
    private boolean success;

    @Getter
    private long lastUpdated;

    @Getter
    private String version;

    @Getter
    private Map<String, Skill> collections;

    @Getter
    private Map<String, Skill> skills;

    public static class Skill {
        @Getter
        private String name;

        @Getter
        private String description;

        @Getter
        private int maxLevel;

        @Getter
        private List<SkillLevel> levels;
    }

    public static class SkillLevel {
        @Getter
        private int level;

        @Getter
        private double totalExpRequired;

        @Getter
        private List<String> unlocks;
    }
}
