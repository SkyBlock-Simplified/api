package dev.sbs.api.client.hypixel.response.hypixel;

import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

public class HypixelStatusResponse {

    @Getter private boolean success;
    private UUID uuid;
    @Getter private Session session;

    public static class Session {

        @Getter private boolean online;
        private String gameType;
        private String mode;

        public Optional<String> getGameType() {
            return Optional.ofNullable(this.gameType);
        }

        public Optional<String> getMode() {
            return Optional.ofNullable(this.mode);
        }

    }

}
