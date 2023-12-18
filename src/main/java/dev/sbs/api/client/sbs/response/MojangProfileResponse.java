package dev.sbs.api.client.sbs.response;

import dev.sbs.api.client.mojang.response.MojangPropertiesResponse;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.DataUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class MojangProfileResponse {

    private UUID uniqueId;
    private String username;
    private Instant timestamp;
    private ConcurrentList<String> profileActions = Concurrent.newList();
    private Textures textures;

    public MojangProfileResponse(@NotNull MojangPropertiesResponse properties) {
        this.uniqueId = properties.getUniqueId();
        this.username = properties.getUsername();
        this.timestamp = mapProperty(properties, property -> property.getValue().getTimestamp()).orElse(Instant.now());
        this.profileActions = properties.getProfileActions();
        this.textures = new Textures(properties);
    }

    private static <T> Optional<T> mapProperty(@NotNull MojangPropertiesResponse properties, @NotNull Function<MojangPropertiesResponse.Property, T> function) {
        return properties.getProperties()
            .findFirst()
            .map(function);
    }

    @Getter
    @NoArgsConstructor
    public static class Textures {

        private boolean slim;
        private Raw raw;
        private Value skin;
        private Value cape;

        @SneakyThrows
        public Textures(@NotNull MojangPropertiesResponse properties) {
            MojangPropertiesResponse.Property property = properties.getProperty();
            MojangPropertiesResponse.Property.Value value = property.getValue();
            this.slim = value.isSlim();
            this.raw = new Raw(
                property.getRaw(),
                property.getSignature()
            );
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
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Raw {

            private String value;
            private Optional<String> signature = Optional.empty();

        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Value {

            private Optional<String> url = Optional.empty();
            private Optional<String> data = Optional.empty();

        }

    }

}
