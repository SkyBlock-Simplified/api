package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.IntStream;

public abstract class Experience {

    public int getStartingLevel() {
        return 0;
    }

    public abstract double getExperience();

    public abstract ConcurrentList<Double> getExperienceTiers();

    public int getLevelSubtractor() {
        return 0;
    }

    public final int getLevel() {
        return this.getLevel(this.getExperience());
    }

    public final int getLevel(double experience) {
        return Math.min(this.getRawLevel(experience), this.getMaxLevel() - this.getLevelSubtractor());
    }

    public final int getRawLevel() {
        return this.getRawLevel(this.getExperience());
    }

    public final int getRawLevel(double experience) {
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();

        return IntStream.range(0, experienceTiers.size())
            .filter(index -> experienceTiers.get(index) > experience)
            .findFirst()
            .orElseGet(this::getMaxLevel);
    }

    public abstract int getMaxLevel();

    public final double getNextExperience() {
        return this.getNextExperience(this.getExperience());
    }

    public final double getNextExperience(double experience) {
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        int rawLevel = this.getRawLevel(experience);

        if (rawLevel == 0)
            return experienceTiers.get(0);
        else if (rawLevel >= this.getMaxLevel())
            return 0;
        else
            return experienceTiers.get(rawLevel) - experienceTiers.get(rawLevel - 1);
    }

    public final double getProgressExperience() {
        return this.getProgressExperience(this.getExperience());
    }

    public final double getProgressExperience(double experience) {
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        int rawLevel = this.getRawLevel(experience);

        try {
            if (rawLevel == 0)
                return experience;
            else if (rawLevel >= this.getMaxLevel())
                return experience - experienceTiers.get(experienceTiers.size() - 1);
        } catch (Exception ignore) { }

        return experience - experienceTiers.get(rawLevel - 1);
    }

    public final double getMissingExperience() {
        return this.getMissingExperience(this.getExperience());
    }

    public final double getMissingExperience(double experience) {
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        int rawLevel = this.getRawLevel(experience);
        return rawLevel >= this.getMaxLevel() ? 0 : (experienceTiers.get(rawLevel) - experience);
    }

    public final double getProgressPercentage() {
        return this.getProgressPercentage(this.getExperience());
    }

    public final double getProgressPercentage(double experience) {
        double progressExperience = this.getProgressExperience(experience);
        double nextExperience = this.getNextExperience(experience);
        return nextExperience == 0 ? 100.0 : (progressExperience / nextExperience) * 100.0;
    }

    public final double getTotalExperience() {
        return this.getExperienceTiers()
            .stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }

    public final double getTotalProgressPercentage() {
        return (this.getExperience() / this.getTotalExperience()) * 100.0;
    }

    @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Weight {

        @Getter private final double value;
        @Getter private final double overflow;

        public final double getTotal() {
            return this.getValue() + this.getOverflow();
        }

    }

}
