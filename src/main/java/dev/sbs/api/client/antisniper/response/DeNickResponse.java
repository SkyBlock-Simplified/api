package dev.sbs.api.client.antisniper.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DeNickResponse {

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

        @SerializedName("queried_nick")
        @Getter private String queriedNick;
        @SerializedName("latest_nick")
        @Getter private String latestNick;
        @SerializedName("nick_in_pool")
        @Getter private boolean nickInPool;

        public Instant getFirstDetected() {
            return Instant.ofEpochSecond(this.firstDetected);
        }

        public Instant getLastSeen() {
            return Instant.ofEpochSecond(this.lastSeen);
        }

    }

}
