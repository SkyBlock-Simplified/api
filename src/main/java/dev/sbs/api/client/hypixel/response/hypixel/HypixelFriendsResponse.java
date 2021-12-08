package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class HypixelFriendsResponse {

    @Getter
    private boolean success;
    private String uuid;
    @Getter
    private ConcurrentList<Record> records;

    public UUID getUniqueId() {
        return StringUtil.toUUID(this.uuid);
    }

    public static class Record {

        @SerializedName("_id")
        @Getter
        private String friendId;
        private String uuidSender;
        private String uuidReceiver;
        @Getter
        private Instant started;

        public UUID getReceiverUniqueId() {
            return StringUtil.toUUID(this.uuidReceiver);
        }

        public UUID getSenderUniqueId() {
            return StringUtil.toUUID(this.uuidSender);
        }

    }

}
