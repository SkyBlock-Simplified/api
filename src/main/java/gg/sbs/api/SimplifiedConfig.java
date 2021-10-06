package gg.sbs.api;

import gg.sbs.api.data.yaml.YamlConfig;
import gg.sbs.api.util.ResourceUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class SimplifiedConfig extends YamlConfig {

    public SimplifiedConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    @Getter @Setter
    private String githubUser = ResourceUtil.getEnvironmentVariable("GITHUB_USER");

    @Getter @Setter
    private String githubToken = ResourceUtil.getEnvironmentVariable("GITHUB_TOKEN");

    @Getter @Setter
    private String databaseHost = ResourceUtil.getEnvironmentVariable("DATABASE_HOST");

    @Getter @Setter
    private Integer databasePort = Integer.parseInt(ResourceUtil.getEnvironmentVariable("DATABASE_PORT"));

    @Getter @Setter
    private String databaseSchema = ResourceUtil.getEnvironmentVariable("DATABASE_SCHEMA");

    @Getter @Setter
    private String databaseUser = ResourceUtil.getEnvironmentVariable("DATABASE_USER");

    @Getter @Setter
    private String databasePassword = ResourceUtil.getEnvironmentVariable("DATABASE_PASSWORD");

    @Getter @Setter
    private boolean databaseDebugMode = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_DEBUG"));

}