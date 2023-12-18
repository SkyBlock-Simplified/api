package dev.sbs.api.text;

import dev.sbs.api.client.sbs.SbsClient;
import dev.sbs.api.client.sbs.request.SbsMojangRequest;
import dev.sbs.api.client.sbs.response.MojangProfileResponse;
import dev.sbs.api.minecraft.image.MinecraftHead;
import dev.sbs.api.minecraft.image.MinecraftText;
import dev.sbs.api.minecraft.text.ChatFormat;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.minecraft.text.segment.TextSegment;
import dev.sbs.api.util.helper.DataUtil;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

public class MinecraftImageGenTest {

    @Test
    public void generateHead() {
        String username = "GoldenDusk";
        MojangProfileResponse mojangProfile = new SbsClient().build(SbsMojangRequest.class).getProfileFromUsername(username);
        String textureUrl = mojangProfile.getTextures().getSkin().getUrl();
        String base64Skin = mojangProfile.getTextures().getSkin().getData();
        System.out.println(textureUrl);

        try {
            URL url = new URL(textureUrl);
            BufferedImage urlImage = ImageIO.read(url);
            MinecraftHead minecraftHead = new MinecraftHead(urlImage).drawHead();
            //File tempFile = minecraftHead.toFile();
            //System.out.println(tempFile.getAbsolutePath());

            byte[] data = DataUtil.decode(base64Skin);
            BufferedImage b64Image = ImageIO.read(new ByteArrayInputStream(data));
            MinecraftHead minecraftHead2 = new MinecraftHead(b64Image).drawHead();
            File tempFile2 = minecraftHead2.toFile();
            System.out.println(tempFile2.getAbsolutePath());
        } catch (Exception ignore) { }
    }

    @Test
    public void generateTextLine() {
        MinecraftText text = MinecraftText.builder()
            .withAlpha(255)
            .withSegments(
                TextSegment.builder()
                    .withColor(ChatFormat.GOLD)
                    .withText("CraftedFury")
                    .build(),
                TextSegment.builder()
                    .withColor(ChatFormat.WHITE)
                    .withText(": Hello World!")
                    .build()
            )
            .withPadding(0)
            .build()
            .render();

        File tempFile = text.toFile();
        System.out.println(tempFile.getAbsolutePath());
    }

    @Test
    public void generateFakeItem() {
        MinecraftText text = MinecraftText.builder()
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.AQUA)
                    .withText("Hermes' Slippers")
                    .build()
            )
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("Speed: ")
                    .build(),
                ColorSegment.builder()
                    .withColor(ChatFormat.RED)
                    .withText("∞")
                    .build()
            )
            .withEmptyLine()
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.GRAY)
                    .withText("You only wish your text")
                    .isUnderlined()
                    .build()
            )
            .withSegments(
                ColorSegment.builder()
                    .withText("could look this cool!")
                    .isUnderlined()
                    .build()
            )
            .withEmptyLine()
            .withSegments(
                ColorSegment.builder()
                    .withColor(ChatFormat.AQUA)
                    .withText("DIVINE")
                    .isStrikethrough()
                    .isBold()
                    .build(),
                ColorSegment.builder()
                    .withColor(ChatFormat.AQUA)
                    .withText(" GOD BOOTS")
                    .isBold()
                    .build()
            )
            .withPadding(0)
            .build()
            .render();

        /*JLabel picLabel = new JLabel(new ImageIcon(text.getImage()));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        jPanel.setVisible(true);
        JFrame f = new JFrame();
        int width = text.getImage().getWidth();
        int height = text.getImage().getHeight();
        f.setSize(new Dimension(width + width / 2, height + height / 2));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);*/

        File tempFile = text.toFile();
        System.out.println(tempFile.getAbsolutePath());
    }

}
