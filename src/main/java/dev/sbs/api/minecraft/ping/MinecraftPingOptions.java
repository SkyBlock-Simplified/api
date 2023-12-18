package dev.sbs.api.minecraft.ping;

import lombok.Builder;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Storage class for {@link MinecraftPing} options.
 */
@Getter
@Builder
public class MinecraftPingOptions {

    private String hostname;

    @Builder.Default
    private Charset charset = StandardCharsets.UTF_8;

    @Builder.Default
    private int port = 25565;

    @Builder.Default
    private int timeout = 5000;

}
