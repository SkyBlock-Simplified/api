package dev.sbs.api.minecraft.ping;

import lombok.Builder;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Storage class for {@link MinecraftPing} options.
 */
@Builder
public class MinecraftPingOptions {

    @Getter private String hostname;

    @Builder.Default
    @Getter private Charset charset = StandardCharsets.UTF_8;

    @Getter
    @Builder.Default
    private int port = 25565;

    @Getter
    @Builder.Default
    private int timeout = 5000;

}