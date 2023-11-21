package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.gson.SerializedPath;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JacobsFarming {

    @SerializedName("medals_inv")
    private @NotNull ConcurrentMap<Medal, Integer> medals = Concurrent.newMap();
    @SerializedPath("perks.double_drops")
    private int doubleDrops;
    @SerializedPath("perks.farming_level_cap")
    private int farmingLevelCap;
    @Accessors(fluent = true)
    @SerializedName("talked")
    private boolean hasTalked;
    @Getter(AccessLevel.NONE)
    @SerializedName("contests")
    private @NotNull ConcurrentMap<String, Contest> contestMap = Concurrent.newMap();
    private @NotNull ConcurrentList<Contest> contestList = Concurrent.newList();
    @SerializedName("unique_brackets")
    private @NotNull ConcurrentMap<Medal, ConcurrentList<String>> uniqueBrackets = Concurrent.newMap();
    private boolean migration;
    @SerializedName("personal_bests")
    private @NotNull ConcurrentMap<String, Integer> personalBests = Concurrent.newMap();

    public @NotNull ConcurrentList<Contest> getContests() {
        if (ListUtil.isEmpty(this.contestList)) {
            this.contestList = this.contestMap.stream()
                .map(entry -> {
                    Contest contest = entry.getValue();

                    String[] dataString = entry.getKey().split(":");
                    String[] calendarString = dataString[1].split("_");
                    int year = NumberUtil.toInt(dataString[0]);
                    int month = NumberUtil.toInt(calendarString[0]);
                    int day = NumberUtil.toInt(calendarString[1]);
                    String collectionName = StringUtil.join(dataString, ":", 2, dataString.length);

                    contest.skyBlockDate = new SkyBlockDate(year, month, day);
                    contest.collectionName = collectionName;
                    return contest;
                })
                .collect(Concurrent.toList())
                .toUnmodifiableList();
        }

        return this.contestList;
    }

    public enum Medal {

        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        DIAMOND

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Contest {

        private int collected;
        @Accessors(fluent = true)
        @SerializedName("claimed_rewards")
        private boolean hasClaimedRewards;
        @SerializedName("claimed_position")
        private int position;
        @SerializedName("claimed_participants")
        private int participants;
        private SkyBlockDate skyBlockDate;
        private String collectionName;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Data {

            private SkyBlockDate skyBlockDate;
            private String collectionName;

        }

    }

}
