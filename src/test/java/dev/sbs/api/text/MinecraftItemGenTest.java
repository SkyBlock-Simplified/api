package dev.sbs.api.text;

import dev.sbs.api.minecraft.text.ChatFormat;
import dev.sbs.api.minecraft.text.MinecraftText;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import org.junit.jupiter.api.Test;

import java.io.File;

public class MinecraftItemGenTest {

    @Test
    public void generateFakeItem() {
        MinecraftText text = MinecraftText.builder()
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("Sample Item")
                    .build()
            )
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("abc1 ABC1")
                    .isUnderlined()
                    .build()
            )
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("abc2 ABC2")
                    .isStrikethrough()
                    .build()
            )
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("abc3 ABC3")
                    .isStrikethrough()
                    .isUnderlined()
                    .build()
            )
            .withPadding(1)
            .build();

        File tempFile = text.render().toFile();
        System.out.println(tempFile.getAbsolutePath());
    }

}
