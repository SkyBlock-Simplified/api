package dev.sbs.api;

import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.util.helper.ResourceUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class SimplifiedConfig extends SqlConfig {

    @Getter
    @Setter
    private String githubUser = ResourceUtil.getEnv("GITHUB_USER").orElse("");

    @Getter
    @Setter
    private String githubToken = ResourceUtil.getEnv("GITHUB_TOKEN").orElse("");

    @Getter
    private Optional<UUID> hypixelApiKey = ResourceUtil.getEnv("HYPIXEL_API_KEY").map(StringUtil::toUUID);

    public SimplifiedConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    public void setHypixelApiKey(String apiKey) {
        if (StringUtil.isNotEmpty(apiKey))
            this.setHypixelApiKey(StringUtil.toUUID(apiKey));
    }

    public void setHypixelApiKey(UUID apiKey) {
        this.hypixelApiKey = Optional.ofNullable(apiKey);
    }

}
