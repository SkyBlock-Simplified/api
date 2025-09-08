package dev.sbs.api.data.json;

import dev.sbs.api.builder.ClassBuilder;
import dev.sbs.api.builder.annotation.BuildFlag;
import dev.sbs.api.data.DataConfig;
import dev.sbs.api.data.DataSession;
import dev.sbs.api.reflection.Reflection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.StandardLevel;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonConfig extends DataConfig<JsonModel> {

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Override
    protected @NotNull DataSession<JsonModel> createSession() {
        return new JsonSession(this);
    }

    public static @NotNull JsonConfig defaultJson() {
        return builder()
            .withLogLevel(StandardLevel.WARN)
            .build();
    }

    @Override
    protected void onLogLevelChange(@NotNull Level level) {
        // Set Logging Level
        Configurator.setLevel("org.jboss.logging", this.getLogLevel());
        Configurator.setLevel("ch.qos.logback", this.getLogLevel());
    }

    public static class Builder implements ClassBuilder<JsonConfig> {

        // Required Settings

        @BuildFlag(nonNull = true)
        private StandardLevel logLevel = StandardLevel.WARN;


        public Builder withLogLevel(@NotNull StandardLevel level) {
            this.logLevel = level;
            return this;
        }

        @Override
        public @NotNull JsonConfig build() {
            Reflection.validateFlags(this);

            JsonConfig jsonConfig = new JsonConfig();

            jsonConfig.setLogLevel(Level.toLevel(this.logLevel.name()));
            return jsonConfig;
        }

    }

}
