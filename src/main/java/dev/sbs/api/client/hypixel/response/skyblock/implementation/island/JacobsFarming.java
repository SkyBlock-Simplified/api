package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.collection.concurrent.ConcurrentSet;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemModel;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JacobsFarming {

    @SerializedName("medals_inv")
    @Getter private ConcurrentMap<Medal, Integer> medalInventory = Concurrent.newMap();
    @Getter private ConcurrentMap<Perk, Integer> perks = Concurrent.newMap();
    @SerializedName("unique_golds2")
    private ConcurrentSet<String> uniqueGolds = Concurrent.newSet();
    private ConcurrentMap<String, Contest> contests = Concurrent.newMap();
    private ConcurrentList<Contest> contestData = Concurrent.newList();
    private boolean talked;

    public ConcurrentList<Contest> getContests() {
        if (ListUtil.isEmpty(this.contestData)) {
            this.contests.forEach(entry -> {
                Contest contest = entry.getValue();

                String[] dataString = entry.getKey().split(":");
                String[] calendarString = dataString[1].split("_");
                int year = NumberUtil.toInt(dataString[0]);
                int month = NumberUtil.toInt(calendarString[0]);
                int day = NumberUtil.toInt(calendarString[1]);
                String collectionName = StringUtil.join(dataString, ":", 2, dataString.length);

                contest.skyBlockDate = new SkyBlockDate(year, month, day);
                contest.collectionName = collectionName;
                this.contestData.add(contest);
            });

            this.contestData = Concurrent.newUnmodifiableList(this.contestData);
        }

        return this.contestData;
    }

    public int getMedals(Medal medal) {
        return this.getMedalInventory().getOrDefault(medal, 0);
    }

    public int getPerk(Perk perk) {
        return this.getPerks().getOrDefault(perk, 0);
    }

    public ConcurrentSet<CollectionItemModel> getUniqueGolds() {
        return SimplifiedApi.getRepositoryOf(CollectionItemModel.class)
            .stream()
            .filter(collectionItem -> uniqueGolds.contains(collectionItem.getItem().getItemId()))
            .collect(Concurrent.toSet());
    }

    public boolean hasTalked() {
        return this.talked;
    }

    public enum Medal {

        @SerializedName("bronze")
        BRONZE,
        @SerializedName("silver")
        SILVER,
        @SerializedName("gold")
        GOLD

    }

    public enum Perk {

        @SerializedName("double_drops")
        DOUBLE_DROPS,
        @SerializedName("farming_level_cap")
        FARMING_LEVEL_CAP

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Contest {

        @Getter private int collected;
        @SerializedName("claimed_rewards")
        private boolean claimedRewards;
        @SerializedName("claimed_position")
        @Getter private int position;
        @SerializedName("claimed_participants")
        @Getter private int participants;

        @Getter private SkyBlockDate skyBlockDate;
        @Getter private String collectionName;

        public boolean hasClaimedRewards() {
            return this.claimedRewards;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Data {

            @Getter private SkyBlockDate skyBlockDate;
            @Getter private String collectionName;

        }

    }

}
