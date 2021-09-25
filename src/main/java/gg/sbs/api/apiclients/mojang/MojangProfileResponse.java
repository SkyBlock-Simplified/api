package gg.sbs.api.apiclients.mojang;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

public class MojangProfileResponse {
    private String uuid;
    private String username;
    @SerializedName("username_history")
    private List<MojangUsernameChange> usernameHistory;
    private MojangTextures texture;
    @SerializedName("created_at")
    private Instant createdAt;

    public static class MojangUsernameChange {
        private String username;
        private Instant changedAt;

        public String getUsername() {
            return username;
        }

        public Instant getChangedAt() {
            return changedAt;
        }
    }

    public static class MojangTextures {
        private boolean custom;
        private boolean slim;
        private MojangSkin skin;
        private MojangRaw raw;

        public boolean isCustom() {
            return custom;
        }

        public boolean isSlim() {
            return slim;
        }

        public MojangSkin getSkin() {
            return skin;
        }

        public MojangRaw getRaw() {
            return raw;
        }
    }

    public static class MojangSkin {
        private String url;
        private String data;

        public String getUrl() {
            return url;
        }

        public String getData() {
            return data;
        }
    }

    public static class MojangRaw {
        private String value;
        private String signature;

        public String getValue() {
            return value;
        }

        public String getSignature() {
            return signature;
        }
    }
}
