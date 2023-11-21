package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.tuple.Pair;
import lombok.Getter;

import java.util.Arrays;

public abstract class StatData<T extends ObjectData.Type> {

    @Getter protected final ConcurrentMap<T, ConcurrentLinkedMap<StatModel, Data>> stats = Concurrent.newMap();

    public final Data getAllData(StatModel statModel) {
        Data statData = new Data();

        this.stats.forEach((type, statEntries) -> statEntries.stream()
            .filter(statModelDataEntry -> statModelDataEntry.getKey().equals(statModel))
            .forEach(statModelDataEntry -> {
                statData.addBase(statModelDataEntry.getValue().getBase());
                statData.addBonus(statModelDataEntry.getValue().getBonus());
            }));

        return statData;
    }

    protected final void addBase(Data data, double value) {
        data.addBase(value);
    }

    protected final void addBonus(Data data, double value) {
        data.addBonus(value);
    }

    protected abstract T[] getAllTypes();

    @SafeVarargs
    public final Data getData(StatModel statModel, T... types) {
        return this.getStatsOf(types).get(statModel);
    }

    public final ConcurrentLinkedMap<StatModel, Data> getAllStats() {
        return this.getStatsOf(this.getAllTypes());
    }

    public final ConcurrentLinkedMap<StatModel, Data> getStats(T type) {
        return this.stats.get(type);
    }

    @SafeVarargs
    public final ConcurrentLinkedMap<StatModel, Data> getStatsOf(T... types) {
        ConcurrentLinkedMap<StatModel, Data> totalStats = SimplifiedApi.getRepositoryOf(StatModel.class)
            .findAll()
            .sorted(StatModel::getOrdinal)
            .stream()
            .map(statModel -> Pair.of(statModel, new Data()))
            .collect(Concurrent.toLinkedMap());

        Arrays.stream(types)
            .flatMap(type -> this.stats.get(type).stream())
            .forEach(entry -> {
                Data statData = totalStats.get(entry.getKey());
                statData.addBase(entry.getValue().getBase());
                statData.addBonus(entry.getValue().getBonus());
            });

        return totalStats;
    }

    protected final void setBase(Data data, double value) {
        data.base = value;
    }

    protected final void setBonus(Data data, double value) {
        data.bonus = value;
    }

}
