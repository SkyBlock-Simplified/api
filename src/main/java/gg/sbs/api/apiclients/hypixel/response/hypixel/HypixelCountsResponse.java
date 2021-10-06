package gg.sbs.api.apiclients.hypixel.response.hypixel;

import gg.sbs.api.util.concurrent.ConcurrentMap;
import lombok.Getter;

import java.util.Optional;

public class HypixelCountsResponse {

    @Getter private boolean success;
    @Getter private int playerCount;
    @Getter private ConcurrentMap<String, String> games;

    public static class Game {

        @Getter private int players;
        private ConcurrentMap<String, Integer> modes;

        public Optional<ConcurrentMap<String, Integer>> getModes() {
            return Optional.of(this.modes);
        }

    }

}