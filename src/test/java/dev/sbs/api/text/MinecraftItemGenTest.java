package dev.sbs.api.text;

import dev.sbs.api.minecraft.text.ChatFormat;
import dev.sbs.api.minecraft.text.MinecraftText;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import org.junit.jupiter.api.Test;

import java.io.File;

public class MinecraftItemGenTest {

    @Test
    public void generateFakeItem() {
        MinecraftText text = MinecraftText.builder()
            .withSegments(
                Concurrent.newList(
                    ColorSegment.builder()
                        .withColor(ChatFormat.LIGHT_PURPLE)
                        .withText("Terminator")
                        .build()
                )
            )
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("Test")
                    .build()
            )
            .withSegments(ColorSegment.builder().withText("").build()) // Empty Line
            .withSegments(
                ColorSegment.builder()
                    .isBold()
                    .withColor(ChatFormat.LIGHT_PURPLE)
                    .withText("MYTHIC")
                    .build()
            )
            .withPadding(1)
            .build();

        File tempFile = text.render().toFile();
        System.out.println(tempFile.getAbsolutePath());
    }

}
