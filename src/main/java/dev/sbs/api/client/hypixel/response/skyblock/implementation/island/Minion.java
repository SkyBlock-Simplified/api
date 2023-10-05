package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;

import java.util.Comparator;

@Getter
public class Minion {

    private final MinionModel type;
    private ConcurrentList<Integer> unlocked = Concurrent.newUnmodifiableList();

    private Minion(MinionModel type) {
        this.type = type;
    }
    
    Minion(MinionModel type, ConcurrentList<String> craftedMinions) {
        this(type);

        this.unlocked = Concurrent.newUnmodifiableList(
            craftedMinions.stream()
                .filter(item -> item.matches(String.format("^%s_[\\d]+$", this.getType().getKey())))
                .map(item -> Integer.parseInt(item.replace(String.format("%s_", this.getType().getKey()), "")))
                .collect(Concurrent.toList())
                .sorted(Comparator.naturalOrder())
        );
    }

    static Minion merge(MinionModel type, ConcurrentList<Integer> unlocked) {
        Minion minion = new Minion(type);
        minion.unlocked = Concurrent.newUnmodifiableList(unlocked);
        return minion;
    }

}
