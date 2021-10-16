package gg.sbs.api;

import gg.sbs.api.data.sql.SqlConfig;
import gg.sbs.api.util.helper.ResourceUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class SimplifiedConfig extends SqlConfig {

    public SimplifiedConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    @Getter @Setter
    private String githubUser = ResourceUtil.getEnvironmentVariable("GITHUB_USER");

    @Getter @Setter
    private String githubToken = ResourceUtil.getEnvironmentVariable("GITHUB_TOKEN");

    @Getter @Setter
    private String hypixelApiKey = ResourceUtil.getEnvironmentVariable("HYPIXEL_API_KEY");

}