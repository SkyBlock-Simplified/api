package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;

public class TrophyFish {

    @Getter private final int totalCaught;

    TrophyFish(ConcurrentMap<String, Object> trophy_fish) {
        trophy_fish.remove("rewards");
        this.totalCaught = (int) trophy_fish.removeOrGet("total_caught", 0);

        // TODO: Trophy Fish Database Table
    }

}
