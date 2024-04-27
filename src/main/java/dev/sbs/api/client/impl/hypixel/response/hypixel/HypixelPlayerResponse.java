package dev.sbs.api.client.impl.hypixel.response.hypixel;

import dev.sbs.api.client.impl.hypixel.response.hypixel.implementation.HypixelPlayer;
import lombok.Getter;

import java.util.Optional;

@Getter
public class HypixelPlayerResponse {

    private boolean success;
    private Optional<HypixelPlayer> player = Optional.empty();

}
