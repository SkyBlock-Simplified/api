package dev.sbs.api.client.impl.hypixel.response.hypixel;

import dev.sbs.api.client.impl.hypixel.response.hypixel.implementation.HypixelPlayer;
import lombok.Getter;

@Getter
public class HypixelPlayerResponse {

    private boolean success;
    private HypixelPlayer player;

}