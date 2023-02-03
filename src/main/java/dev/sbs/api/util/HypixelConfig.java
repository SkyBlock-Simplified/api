package dev.sbs.api.util;

import dev.sbs.api.data.yaml.YamlConfig;
import dev.sbs.api.util.helper.ResourceUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public final class HypixelConfig extends YamlConfig {

    @Getter private Optional<UUID> hypixelApiKey = ResourceUtil.getEnv("HYPIXEL_API_KEY").map(StringUtil::toUUID);
    @Getter private Optional<UUID> antiSniperApiKey = ResourceUtil.getEnv("ANTISNIPER_API_KEY").map(StringUtil::toUUID);

    public HypixelConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    public void setAntiSniperApiKey(@Nullable String antiSniperApiKey) {
        if (StringUtil.isNotEmpty(antiSniperApiKey))
            this.setAntiSniperApiKey(StringUtil.toUUID(antiSniperApiKey));
    }

    public void setAntiSniperApiKey(@Nullable UUID antiSniperApiKey) {
        this.setAntiSniperApiKey(Optional.ofNullable(antiSniperApiKey));
    }

    public void setAntiSniperApiKey(@NotNull Optional<UUID> antiSniperApiKey) {
        this.antiSniperApiKey = antiSniperApiKey;
    }

    public void setHypixelApiKey(@Nullable String hypixelApiKey) {
        if (StringUtil.isNotEmpty(hypixelApiKey))
            this.setHypixelApiKey(StringUtil.toUUID(hypixelApiKey));
    }

    public void setHypixelApiKey(@Nullable UUID hypixelApiKey) {
        this.setHypixelApiKey(Optional.ofNullable(hypixelApiKey));
    }

    public void setHypixelApiKey(@NotNull Optional<UUID> hypixelApiKey) {
        this.hypixelApiKey = hypixelApiKey;
    }

}