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
        @Getter private String guildId;
        @Getter private String name;
        private String tag;
        private String description;
        @Getter private long chatMute;
        @Getter private int coins;
        @Getter private int coinsEver;
        @Getter private Instant created;
        @Getter private boolean publiclyListed;
        private MinecraftChatFormatting tagColor;
        @Getter private ConcurrentList<Member> members;
        @Getter private ConcurrentList<Rank> ranks;
        @Getter private ConcurrentMap<String, Integer> achievements;
        @Getter private ConcurrentList<String> preferredGames;
        @SerializedName("guildExpByGameType")
        @Getter private ConcurrentMap<String, Long> experienceByGameType;

        public Member getGuildMaster() {
            return this.getMembers()
                .stream()
                .filter(member -> member.rank.equals("Guild Master"))
                .findFirst()
                .orElseThrow(IllegalStateException::new); // Will Never Throw
        }

        public Optional<String> getTag() {
            return Optional.ofNullable(this.tag);
        }

        public Optional<String> getDescription() {
            return Optional.ofNullable(this.description);
        }

        public Optional<MinecraftChatFormatting> getTagColor() {
            return Optional.ofNullable(this.tagColor);
        }

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
                if ("Guild Master".equals(this.rank))
                    return Rank.buildGM(Guild.this.created);
                else
                    return Guild.this.ranks.stream().filter(rank -> rank.getName().equals(this.rank)).collect(ListUtil.toSingleton());
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

            private static Rank buildGM(Instant created) {
                Rank rank = new Rank();
                rank.name = "Guild Master";
                rank.tag = "GM";
                rank.created = created;
                rank.priority = 10;
                rank.isDefault = false;
                return rank;
            }

        }

    }

}
