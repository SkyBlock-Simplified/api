package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_powers.AccessoryPowerModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class AccessoryBag {

    @Getter private final NbtContent contents;
    @Getter private final int purchasedBagUpgrades;
    @Getter private final Tuning tuning;
    private final String selectedPower;
    private final ConcurrentMap<Integer, String> unlockedPowers;

    AccessoryBag(NbtContent contents, ConcurrentMap<String, Object> accessoryMap) {
        this.contents = contents;
        this.purchasedBagUpgrades = (int) accessoryMap.getOrDefault("bag_upgrades_purchased", 0);
        this.selectedPower = (String) accessoryMap.getOrDefault("selected_power", "");
        this.unlockedPowers = Concurrent.newMap((Map<Integer, String>) accessoryMap.getOrDefault("unlocked_powers", Concurrent.newMap()));

        if (accessoryMap.containsKey("tuning")) {
            Map<String, Object> tuningMap = (Map<String, Object>) accessoryMap.get("tuning");
            ConcurrentMap<String, Object> tuningCMap = Concurrent.newMap(tuningMap);
            this.tuning = new Tuning(tuningCMap);
        } else
            this.tuning = new Tuning(Concurrent.newMap());
    }

    public Optional<AccessoryPowerModel> getSelectedPower() {
        return SimplifiedApi.getRepositoryOf(AccessoryPowerModel.class).findFirst(AccessoryPowerModel::getKey, selectedPower.toUpperCase());
    }

    public ConcurrentList<AccessoryPowerModel> getUnlockedPowers() {
        return SimplifiedApi.getRepositoryOf(AccessoryPowerModel.class)
            .findAll()
            .stream()
            .filter(accessoryPowerModel -> this.unlockedPowers.containsValue(accessoryPowerModel.getKey().toLowerCase()))
            .collect(Concurrent.toList());
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Tuning {

        @Getter private final ConcurrentMap<StatModel, Integer> current;
        @Getter private final ConcurrentList<ConcurrentMap<StatModel, Integer>> slots;
        @Getter private final int highestUnlockedSlot;

        private Tuning(ConcurrentMap<String, Object> tuningMap) {
            this.highestUnlockedSlot = (int) tuningMap.removeOrGet("highest_unlocked_slot", 0);
            ConcurrentList<ConcurrentMap<StatModel, Integer>> tuningSlots = Concurrent.newList();

            for (int i = 0; i <= 5; i++) {
                String slotName = FormatUtil.format("slot_{0}", i);

                if (tuningMap.containsKey(slotName)) {
                    tuningSlots.add(
                        ((Map<String, Double>) tuningMap.get(slotName)).entrySet()
                            .stream()
                            .map(entry -> Pair.of(
                                SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getKey, entry.getKey().toUpperCase()),
                                entry.getValue()
                            ))
                            .collect(Concurrent.toMap())
                    );
                }
            }

            this.current = ListUtil.notEmpty(tuningSlots) ? tuningSlots.removeFirst() : Concurrent.newMap();
            this.slots = tuningSlots;
        }

    }

}