package dev.sbs.api.client.antisniper.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class FindNickResponse {

    @Getter private boolean success;
    private Player player;

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(this.player);
    }

    public static class Player {

        // Common
        @SerializedName("uuid")
        @Getter private UUID uniqueId;
        @SerializedName("ign")
        @Getter private String name;
        @SerializedName("first_detected")
        private long firstDetected;
        @SerializedName("last_seen")
        private long lastSeen;

        @SerializedName("nick")
        @Getter private String nick;

        public Instant getFirstDetected() {
            return Instant.ofEpochSecond(this.firstDetected);
        }

        public Instant getLastSeen() {
            return Instant.ofEpochSecond(this.lastSeen);
        }

    }

}
