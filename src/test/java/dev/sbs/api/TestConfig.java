package dev.sbs.api;

import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.util.helper.ResourceUtil;
import dev.sbs.api.util.helper.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class TestConfig extends SqlConfig {

    private final Optional<UUID> hypixelApiKey = ResourceUtil.getEnv("HYPIXEL_API_KEY").map(StringUtil::toUUID);

    public TestConfig(@NotNull File configDir, @NotNull String fileName, String... header) {
        super(configDir, fileName, header);
        this.hypixelApiKey.ifPresent(value -> SimplifiedApi.getKeyManager().add("HYPIXEL_API_KEY", value));
    }

    public Optional<UUID> getHypixelApiKey() {
        return this.hypixelApiKey;
    }

}
