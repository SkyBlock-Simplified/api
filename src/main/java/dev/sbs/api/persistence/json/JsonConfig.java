package dev.sbs.api.persistence.json;

import dev.sbs.api.builder.ClassBuilder;
import dev.sbs.api.builder.annotation.BuildFlag;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.persistence.Session;
import dev.sbs.api.persistence.SessionConfig;
import dev.sbs.api.reflection.Reflection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonConfig implements SessionConfig<JsonModel> {

    private final @NotNull ConcurrentList<Class<JsonModel>> models;
    private final @NotNull String identifier;
    private @NotNull Level logLevel;

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Override
    public @NotNull Session<JsonModel> createSession() {
        return new JsonSession(this);
    }

    public static @NotNull JsonConfig defaultJson() {
        return builder()
            .withLogLevel(Level.WARN)
            .build();
    }

    @Override
    public void setLogLevel(@NotNull Level level) {
        this.logLevel = level;
        Configurator.setLevel("org.jboss.logging", this.getLogLevel());
        Configurator.setLevel("ch.qos.logback", this.getLogLevel());
    }

    public static class Builder implements ClassBuilder<JsonConfig> {

        // Required Settings

        @BuildFlag(nonNull = true)
        private Level logLevel = Level.WARN;

        public Builder withLogLevel(@NotNull Level level) {
            this.logLevel = level;
            return this;
        }

        @Override
        public @NotNull JsonConfig build() {
            Reflection.validateFlags(this);

            JsonConfig jsonConfig = new JsonConfig(
                SessionConfig.detectModels(Reflection.getSuperClass(JsonConfig.class)),
                UUID.randomUUID().toString(),
                this.logLevel
            );

            jsonConfig.setLogLevel(this.logLevel);
            return jsonConfig;
        }

    }

}
