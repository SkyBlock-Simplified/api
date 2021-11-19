package dev.sbs.api.data.model.skyblock.formats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.minecraft.text.MinecraftChatFormatting;

import java.awt.*;

public interface FormatModel extends Model {

    String getKey();

    char getCode();

    Color getRgb();

    boolean isFormat();

    default MinecraftChatFormatting getChatFormatting() {
        return MinecraftChatFormatting.of(this.getKey());
    }

}
