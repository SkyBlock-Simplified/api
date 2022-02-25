package dev.sbs.api;

import dev.sbs.api.data.sql.SqlConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestConfig extends SqlConfig {

    public TestConfig(@NotNull File configDir, @NotNull String fileName, String... header) {
        super(configDir, fileName, header);
    }

}
