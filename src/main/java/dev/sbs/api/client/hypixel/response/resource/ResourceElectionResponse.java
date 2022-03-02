package dev.sbs.api.client.hypixel.response.resource;

import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceElectionResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private Mayor mayor;

    public Election getElection() {
        return this.getMayor().election;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mayor extends MayorData {

        private Election election;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Candidate extends MayorData {

        @Getter private int votes;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class MayorData {

        @Getter private String key;
        @Getter private String name;
        @Getter private final ConcurrentList<Perk> perks = Concurrent.newList();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Perk {

        @Getter private String name;
        @Getter private String description;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Election {

        @Getter private int year;
        @Getter private final ConcurrentList<Candidate> candidates = Concurrent.newList();

    }

}
