package dev.sbs.api.client.mojang.response;

import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.DataUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Getter
public class MojangProfile {

    private final @NotNull UUID uniqueId;
    private final @NotNull String username;
    private final @NotNull Instant timestamp;
    private final @NotNull ConcurrentList<String> profileActions;
    private final @NotNull Textures textures;

    public MojangProfile(@NotNull MojangPropertiesResponse properties) {
        this.uniqueId = properties.getUniqueId();
        this.username = properties.getUsername();
        this.timestamp = mapProperty(properties, property -> property.getValue().getTimestamp()).orElse(Instant.now());
        this.profileActions = properties.getProfileActions();

        // Set Properties
        this.textures = new Textures(properties);
    }

    private static <T> Optional<T> mapProperty(@NotNull MojangPropertiesResponse properties, @NotNull Function<MojangPropertiesResponse.Property, T> function) {
        return properties.getProperties()
            .findFirst()
            .map(function);
    }

    @Getter
    public static class Textures {

        private final boolean slim;
        private final @NotNull Value skin;
        private final @NotNull Value cape;

        @SneakyThrows
        public Textures(@NotNull MojangPropertiesResponse properties) {
            MojangPropertiesResponse.Property.Value value = properties.getProperty().getValue();
            this.slim = value.isSlim();
            this.skin = new Value(
                value.getSkinUrl(),
                value.getSkinUrl()
                    .map(Textures::getImage)
                    .map(Textures::encodeImage)
            );
            this.cape = new Value(
                value.getCapeUrl(),
                value.getCapeUrl()
                    .map(Textures::getImage)
                    .map(Textures::encodeImage)
            );
        }

        @SneakyThrows
        private static @NotNull BufferedImage getImage(@NotNull String requestUrl) {
            return ImageIO.read(new URL(requestUrl));
        }

        @SneakyThrows
        private static @NotNull String encodeImage(@NotNull BufferedImage bufferedImage) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", outputStream);
            return DataUtil.encodeToString(outputStream.toByteArray());
        }

        @Getter
        @RequiredArgsConstructor
        public static class Value {

            private final @NotNull Optional<String> url;
            private final @NotNull Optional<String> data;

        }

    }

}
