package dev.sbs.api.minecraft.text;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sbs.api.minecraft.text.event.ClickEvent;
import dev.sbs.api.minecraft.text.event.HoverEvent;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public final class MinecraftTextObject {

    private String text;
    private MinecraftChatFormatting color;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private boolean italic, bold, underlined, obfuscated, strikethrough;
    private final ConcurrentList<MinecraftTextObject> extra = Concurrent.newList();

    public MinecraftTextObject(String text) {
        this.setText(text);
    }

    public MinecraftTextBuilder builder() {
        return MinecraftTextBuilder.of(this);
    }

    public void setColor(MinecraftChatFormatting format) {
        Preconditions.checkArgument(format.isColor(), "Format must be a color");
        this.color = format;
    }

    public void setExtra(List<MinecraftTextObject> extra) {
        this.extra.addAll(extra);
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("text", this.getText());

        if (this.getColor() != null) object.addProperty("color", this.getColor().toJsonString());
        if (this.getClickEvent() != null) object.add("clickEvent", this.getClickEvent().toJson());
        if (this.getHoverEvent() != null) object.add("hoverEvent", this.getHoverEvent().toJson());
        if (this.isItalic()) object.addProperty("italic", true);
        if (this.isBold()) object.addProperty("bold", true);
        if (this.isUnderlined()) object.addProperty("underlined", true);
        if (this.isObfuscated()) object.addProperty("obfuscated", true);
        if (this.isStrikethrough()) object.addProperty("strikethrough", true);

        if (ListUtil.notEmpty(this.getExtra())) {
            JsonArray array = new JsonArray();

            for (MinecraftTextObject ex : this.getExtra())
                array.add(ex.toJson());

            object.add("extra", array);
        }

        return object;
    }

    public static MinecraftTextObject fromJson(String jsonString) {
        return fromJson(JsonParser.parseString(jsonString).getAsJsonObject());
    }

    public static MinecraftTextObject fromJson(JsonObject jsonObject) {
        if (jsonObject.has("text")) {
            MinecraftTextObject textObject = new MinecraftTextObject(jsonObject.get("text").getAsString());
            if (jsonObject.has("clickEvent")) textObject.setClickEvent(ClickEvent.fromJson(jsonObject.get("clickEvent").getAsJsonObject()));
            if (jsonObject.has("hoverEvent")) textObject.setHoverEvent(HoverEvent.fromJson(jsonObject.get("hoverEvent").getAsJsonObject()));
            if (jsonObject.has("color")) textObject.setColor(MinecraftChatFormatting.valueOf(jsonObject.get("color").getAsString().toUpperCase()));
            if (jsonObject.has("obfuscated")) textObject.setObfuscated(jsonObject.get("obfuscated").getAsBoolean());
            if (jsonObject.has("italic")) textObject.setItalic(jsonObject.get("italic").getAsBoolean());
            if (jsonObject.has("bold")) textObject.setBold(jsonObject.get("bold").getAsBoolean());
            if (jsonObject.has("underlined")) textObject.setUnderlined(jsonObject.get("underlined").getAsBoolean());

            if (jsonObject.has("extra")) {
                for (JsonElement extra : jsonObject.getAsJsonArray("extra")) {
                    if (extra.isJsonObject()) {
                        MinecraftTextObject e = fromJson(extra.getAsJsonObject());

                        if (e != null)
                            textObject.extra.add(e);
                    }
                }
            }

            return textObject;
        }

        // invalid object
        return null;
    }

    public static MinecraftTextObject fromLegacy(String legacyText) {
        return fromLegacy(legacyText, '&');
    }

    /**
     * This function takes in a legacy text string and converts it into a {@link MinecraftTextObject}.
     * <p>
     * Legacy text strings use the {@link MinecraftChatFormatting#SECTION_SYMBOL}. Many keyboards do not have this symbol however,
     * which is probably why it was chosen. To get around this, it is common practice to substitute
     * the symbol for another, then translate it later. Often '&' is used, but this can differ from person
     * to person. In case the string does not have a {@link MinecraftChatFormatting#SECTION_SYMBOL}, the method also checks for the
     * {@param characterSubstitute}
     *
     * @param legacyText          The text to make into an object
     * @param characterSubstitute The character substitute
     * @return A TextObject representing the legacy text.
     */
    @SuppressWarnings("all")
    public static MinecraftTextObject fromLegacy(String legacyText, char characterSubstitute) {
        MinecraftTextBuilder builder = MinecraftTextBuilder.of("");
        MinecraftTextObject currentObject = new MinecraftTextObject("");
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < legacyText.length(); i++) {
            char c = legacyText.charAt(i);

            if (c == MinecraftChatFormatting.SECTION_SYMBOL || c == characterSubstitute) {
                if ((i + 1) > legacyText.length() - 1)
                    continue; // do nothing.

                // peek at the next character.
                char peek = legacyText.charAt(i + 1);

                if (MinecraftChatFormatting.isValid(peek)) {
                    i += 1; // if valid
                    if (text.length() > 0) {
                        currentObject.setText(text.toString()); // create a new text object
                        builder.appendJson(currentObject); // append the current object.
                        currentObject = new MinecraftTextObject(""); // reset the current object.
                        text.setLength(0); // reset the buffer
                    }

                    MinecraftChatFormatting color = MinecraftChatFormatting.of(peek);

                    switch (color) {
                        case OBFUSCATED:
                            currentObject.setObfuscated(true);
                            break;
                        case BOLD:
                            currentObject.setBold(true);
                            break;
                        case STRIKETHROUGH:
                            currentObject.setStrikethrough(true);
                            break;
                        case ITALIC:
                            currentObject.setItalic(true);
                            break;
                        case UNDERLINE:
                            currentObject.setUnderlined(true);
                            break;
                        case RESET:
                            // Reset everything.
                            currentObject.setColor(MinecraftChatFormatting.WHITE);
                            currentObject.setObfuscated(false);
                            currentObject.setBold(false);
                            currentObject.setItalic(false);
                            currentObject.setUnderlined(false);
                            currentObject.setStrikethrough(false);
                            break;
                        default:
                            // emulate Minecraft's behavior of dropping styles that do not yet have an object.
                            currentObject = new MinecraftTextObject("");
                            currentObject.setColor(color);
                            break;
                    }
                } else
                    text.append(c);
            } else
                text.append(c);
        }

        // whatever we were working on when the loop exited
        currentObject.setText(text.toString());
        builder.appendJson(currentObject);

        return builder.build();
    }

    public String toLegacy() {
        return toLegacy(MinecraftChatFormatting.SECTION_SYMBOL);
    }

    /**
     * Takes an {@link MinecraftTextObject} and transforms it into a legacy string.
     *
     * @param charSubstitute - The substitute character to use if you do not want to use {@link MinecraftChatFormatting#SECTION_SYMBOL}
     * @return A legacy string representation of a text object
     */
    public String toLegacy(char charSubstitute) {
        StringBuilder builder = new StringBuilder();
        if (this.getColor() != null) builder.append(charSubstitute).append(this.getColor().getCode());
        if (this.isObfuscated()) builder.append(charSubstitute).append(MinecraftChatFormatting.OBFUSCATED.getCode());
        if (this.isBold()) builder.append(charSubstitute).append(MinecraftChatFormatting.BOLD.getCode());
        if (this.isStrikethrough()) builder.append(charSubstitute).append(MinecraftChatFormatting.STRIKETHROUGH.getCode());
        if (this.isUnderlined()) builder.append(charSubstitute).append(MinecraftChatFormatting.UNDERLINE.getCode());
        if (this.isItalic()) builder.append(charSubstitute).append(MinecraftChatFormatting.ITALIC.getCode());

        if (this.getColor() == MinecraftChatFormatting.RESET) {
            builder.setLength(0);
            builder.append(charSubstitute).append(MinecraftChatFormatting.RESET.getCode());
        }

        if (StringUtil.isNotEmpty(this.getText()))
            builder.append(this.getText());

        for (MinecraftTextObject extra : this.getExtra())
            builder.append(extra.toLegacy(charSubstitute));

        return builder.toString();
    }

}
