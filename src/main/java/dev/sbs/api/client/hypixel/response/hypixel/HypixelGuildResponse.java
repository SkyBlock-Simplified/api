package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.minecraft.text.MinecraftChatFormatting;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.ListUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HypixelGuildResponse {

    @Getter private boolean success;
    private Guild guild;

    public Optional<Guild> getGuild() {
        return Optional.ofNullable(this.guild);
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
        private long chatMute;
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
            @Getter private UUID uniqueId;
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
