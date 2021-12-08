package dev.sbs.api.client.hypixel.response.hypixel;

import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.util.UUID;

public class HypixelStatusResponse {

    @Getter
    private boolean success;
    private String uuid;
    @Getter
    private Session session;

    public UUID getUniqueId() {
        return StringUtil.toUUID(this.uuid);
    }

    public static class Session {

        @Getter
        private boolean online;
        @Getter
        private String gameType;
        @Getter
        private String mode;

    }

}
