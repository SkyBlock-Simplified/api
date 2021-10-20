package gg.sbs.api;

import com.google.common.base.Preconditions;
import gg.sbs.api.client.hypixel.HypixelApiBuilder;
import gg.sbs.api.data.sql.SqlConfig;
import gg.sbs.api.util.helper.ResourceUtil;
import gg.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class SimplifiedConfig extends SqlConfig {

    public SimplifiedConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    @Getter @Setter
    private String githubUser = ResourceUtil.getEnvironmentVariable("GITHUB_USER");

    @Getter @Setter
    private String githubToken = ResourceUtil.getEnvironmentVariable("GITHUB_TOKEN");

    private String hypixelApiKey = ResourceUtil.getEnvironmentVariable("HYPIXEL_API_KEY");

    public Optional<UUID> getHypixelApiKey() {
        return StringUtil.isNotEmpty(this.hypixelApiKey) ? Optional.of(StringUtil.toUUID(this.hypixelApiKey)) : Optional.empty();
    }

    public void setHypixelApiKey(String apiKey) {
        Preconditions.checkNotNull(apiKey, "Hypixel API key must not be NULL");
        Preconditions.checkArgument(HypixelApiBuilder.apiKeyRegex.matcher(apiKey).matches(), "Hypixel API key must be valid");
        this.hypixelApiKey = apiKey;
    }

    public void setHypixelApiKey(UUID apiKey) {
        Preconditions.checkNotNull(apiKey, "Hypixel API key must not be NULL");
        this.setHypixelApiKey(apiKey.toString());
    }

}