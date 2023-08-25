package dev.sbs.api.client.sbs.response;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class MojangProfileResponse {

    private UUID uuid;
    @Getter
    private String username;
    @SerializedName("username_history")
    @Getter
    private ConcurrentList<UsernameChange> usernameHistory;
    @Getter
    private Textures textures;
    @SerializedName("created_at")
    @Getter
    private Instant createdAt;

    public UUID getUniqueId() {
        return this.uuid;
    }

    public static class Raw {

        @Getter
        private String value;
        @Getter
        private String signature;

    }

    public static class Skin {

        @Getter
        private String url;
        @Getter
        private String data;

    }

    public static class Textures {

        @Getter
        private boolean custom;
        @Getter
        private boolean slim;
        @Getter
        private Skin skin;
        @Getter
        private Raw raw;

    }

    public static class UsernameChange {

        @Getter
        private String username;
        @Getter
        private Instant changedAt;

    }

}
