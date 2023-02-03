package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.NumberUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Skill extends Experience implements Weighted {

    @Getter private final SkillModel type;
    private final double experience;
    private final int levelSubtractor;

    @Override
    public double getExperience() {
        return Math.max(this.experience, 0);
    }

    @Override
    public ConcurrentList<Double> getExperienceTiers() {
        return SimplifiedApi.getRepositoryOf(SkillLevelModel.class)
            .stream()
            .filter(slayerLevel -> slayerLevel.getSkill().getKey().equals(this.getType().getKey()))
            .map(SkillLevelModel::getTotalExpRequired)
            .collect(Concurrent.toList());
    }

    @Override
    public int getLevelSubtractor() {
        return this.getType().getKey().equals("FARMING") ? this.levelSubtractor : super.getLevelSubtractor();
    }

    @Override
    public int getMaxLevel() {
        return this.getType().getMaxLevel();
    }

    @Override
    public Weight getWeight() {
        if (this.getType().getWeightDivider() == 0.00)
            return new Weight(0, 0);

        double rawLevel = this.getRawLevel();
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        double maxSkillExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

        if (rawLevel < this.getMaxLevel())
            rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

        double base = Math.pow(rawLevel * 10, 0.5 + this.getType().getWeightExponent() + (rawLevel / 100.0)) / 1250;
        double weightValue = NumberUtil.round(base, 2);
        double weightOverflow = 0;

        if (this.getExperience() > maxSkillExperienceRequired) {
            double overflow = Math.pow((this.getExperience() - maxSkillExperienceRequired) / this.getType().getWeightDivider(), 0.968);
            weightOverflow = NumberUtil.round(overflow, 2);
        }

        return new Weight(weightValue, weightOverflow);
    }

}
