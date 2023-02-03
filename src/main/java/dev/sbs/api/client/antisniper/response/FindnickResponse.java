package dev.sbs.api.client.antisniper.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class FindnickResponse {

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
        @Getter private Instant firstDetected;
        @SerializedName("last_seen")
        @Getter private Instant lastSeen;

        @SerializedName("nick")
        @Getter private String nick;

    }

}
