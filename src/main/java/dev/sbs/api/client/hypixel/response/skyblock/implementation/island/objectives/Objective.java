package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objectives;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Objective extends BasicObjective {

    @Getter private int progress;

}