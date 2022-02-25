package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.minecraft.text.MinecraftChatFormatting;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class HypixelGuildResponse {

    private boolean success;
    private Guild guild;

    public Guild getGuild() {
        return guild;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class Guild {

        @SerializedName("_id")
        @Getter
        private String guildId;
        @Getter
        private String name;
        @Getter
        private String tag;
        @Getter
        private String description;
        @Getter
        private int chatMute;
        @Getter
        private int coins;
        @Getter
        private int coinsEver;
        @Getter
        private Instant created;
        @Getter
        private boolean publiclyListed;
        @Getter
        private MinecraftChatFormatting tagColor;
        @Getter
        private ConcurrentList<Member> members;
        @Getter
        private ConcurrentList<Rank> ranks;
        @Getter
        private ConcurrentMap<String, Integer> achievements;
        @Getter
        private ConcurrentList<String> preferredGames;
        @SerializedName("guildExpByGameType")
        @Getter
        private ConcurrentMap<String, Integer> experienceByGameType;

        public class Member {

            @SerializedName("uuid")
            private String uniqueId;
            private String rank;
            @Getter
            private Instant joined;
            @Getter
            private int questParticipation;
            @SerializedName("expHistory")
            @Getter
            private Map<String, Integer> experienceHistory;

            public Rank getRank() {
                return Guild.this.ranks.stream().filter(rank -> rank.getName().equals(this.rank) || ("Admin".equals(rank.getName()) && "Guild Master".equals(this.rank))).collect(ListUtil.toSingleton());
            }

            public UUID getUniqueId() {
                return StringUtil.toUUID(this.uniqueId);
            }

        }

        public static class Rank {

            @Getter
            private String name;
            @Getter
            private String tag;
            @Getter
            private Instant created;
            @Getter
            private int priority;
            @SerializedName("default")
            @Getter
            private boolean isDefault;

        }

    }

}
