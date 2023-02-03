package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Weighted;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Slayer extends Experience implements Weighted {

    private final static Reflection<Slayer> slayerRef = Reflection.of(Slayer.class);
    private ConcurrentMap<String, Boolean> claimed_levels = Concurrent.newMap(); // level_#: true
    private int boss_kills_tier_0;
    private int boss_kills_tier_1;
    private int boss_kills_tier_2;
    private int boss_kills_tier_3;
    private int boss_kills_tier_4;
    @SerializedName("xp")
    @Getter private int experience;
    @Getter SlayerModel type;

    private ConcurrentMap<Integer, Boolean> claimed;
    private ConcurrentMap<Integer, Boolean> claimedSpecial;
    private ConcurrentMap<Integer, Integer> kills;

    private void _init() {
        ConcurrentMap<Integer, Boolean> claimed = Concurrent.newMap();
        ConcurrentMap<Integer, Boolean> claimedSpecial = Concurrent.newMap();
        ConcurrentMap<Integer, Integer> kills = Concurrent.newMap();

        for (Map.Entry<String, Boolean> entry : this.claimed_levels.entrySet()) {
            String entryKey = entry.getKey().replace("level_", "");
            boolean special = entryKey.endsWith("_special");
            entryKey = special ? entryKey.replace("_special", "") : entryKey;
            (special ? claimedSpecial : claimed).put(Integer.parseInt(entryKey), entry.getValue());
        }

        for (int i = 0; i < 5; i++)
            kills.put(i + 1, (int) slayerRef.getValue(FormatUtil.format("boss_kills_tier_{0}", i), this));

        this.claimed = Concurrent.newUnmodifiableMap(claimed);
        this.claimedSpecial = Concurrent.newUnmodifiableMap(claimedSpecial);
        this.kills = Concurrent.newUnmodifiableMap(kills);
    }

    public ConcurrentMap<Integer, Boolean> getClaimed() {
        if (Objects.isNull(this.claimed))
            this._init();

        return this.claimed;
    }

    public ConcurrentMap<Integer, Boolean> getClaimedSpecial() {
        if (Objects.isNull(this.claimedSpecial))
            this._init();

        return this.claimedSpecial;
    }

    @Override
    public ConcurrentList<Double> getExperienceTiers() {
        return SimplifiedApi.getRepositoryOf(SlayerLevelModel.class)
            .stream()
            .filter(slayerLevel -> slayerLevel.getSlayer().getKey().equals(this.getType().getKey()))
            .map(SlayerLevelModel::getTotalExpRequired)
            .collect(Concurrent.toList());
    }

    public ConcurrentMap<Integer, Integer> getKills() {
        if (Objects.isNull(this.kills))
            this._init();

        return this.kills;
    }

    public int getKills(int tier) {
        return this.getKills().getOrDefault(tier, 0);
    }

    @Override
    public int getMaxLevel() {
        return 9;
    }

    public boolean isClaimed(int level) {
        return this.getClaimed().get(level);
    }

    @Override
    public Weight getWeight() {
        if (this.getType().getWeightDivider() == 0.00)
            return new Weight(0, 0);

        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        double maxSlayerExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);
        double base = Math.min(this.getExperience(), maxSlayerExperienceRequired) / this.getType().getWeightDivider();
        double weightValue = NumberUtil.round(base, 2);
        double weightOverflow = 0;

        if (this.getExperience() > maxSlayerExperienceRequired) {
            double remaining = this.getExperience() - maxSlayerExperienceRequired;
            double overflow = 0;
            double modifier = this.getType().getWeightModifier();

            while (remaining > 0) {
                double left = Math.min(remaining, maxSlayerExperienceRequired);
                overflow += Math.pow(left / (this.getType().getWeightDivider() * (1.5 + modifier)), 0.942);
                remaining -= left;
                modifier += modifier;
            }

            weightOverflow = NumberUtil.round(overflow, 2);
        }

        return new Weight(weightValue, weightOverflow);
    }

}
