package gg.sbs.api.apiclients.hypixel.response.hypixel;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

public class HypixelCountsResponse {

    @Getter private boolean success;
    @Getter private int playerCount;
    @Getter private Map<String, String> games;

    public static class Game {

        @Getter private int players;
        private Map<String, Integer> modes;

        public Optional<Map<String, Integer>> getModes() {
            return Optional.of(this.modes);
        }

    }

}