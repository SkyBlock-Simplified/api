package dev.sbs.api.client.impl.hypixel.response.hypixel;

import dev.sbs.api.client.impl.hypixel.response.hypixel.implementation.HypixelGuild;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class HypixelGuildResponse {

    private boolean success;
    private @NotNull Optional<HypixelGuild> guild = Optional.empty();

}
