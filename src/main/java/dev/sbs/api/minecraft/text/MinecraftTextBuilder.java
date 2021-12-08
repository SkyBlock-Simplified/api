package dev.sbs.api.minecraft.text;

import dev.sbs.api.minecraft.text.event.ClickEvent;
import dev.sbs.api.minecraft.text.event.HoverEvent;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;

public final class MinecraftTextBuilder implements Builder<MinecraftTextObject> {

    private MinecraftTextObject root;
    private MinecraftTextObject current; // The current text object. This will change when we append text.
    private final ConcurrentList<MinecraftTextObject> extra = Concurrent.newList(); // The storage of extra text objects.

    private MinecraftTextBuilder(MinecraftTextObject root) {
        this.root = root;
        this.current = root;
    }

    public MinecraftTextBuilder color(MinecraftChatFormatting color) {
        this.current.setColor(color);
        return this;
    }

    public MinecraftTextBuilder italicize() {
        return this.italicize(true);
    }

    public MinecraftTextBuilder italicize(boolean value) {
        this.current.setItalic(value);
        return this;
    }

    public MinecraftTextBuilder bold() {
        return this.bold(true);
    }

    public MinecraftTextBuilder bold(boolean value) {
        this.current.setBold(value);
        return this;
    }

    public MinecraftTextBuilder underline() {
        return this.underline(true);
    }

    public MinecraftTextBuilder underline(boolean value) {
        this.current.setUnderlined(value);
        return this;
    }

    public MinecraftTextBuilder obfuscate() {
        return this.obfuscate(true);
    }

    public MinecraftTextBuilder obfuscate(boolean value) {
        this.current.setObfuscated(value);
        return this;
    }

    public MinecraftTextBuilder strikethrough() {
        return this.strikethrough(true);
    }

    public MinecraftTextBuilder strikethrough(boolean value) {
        this.current.setStrikethrough(value);
        return this;
    }

    public MinecraftTextBuilder clickEvent(ClickEvent clickEvent) {
        this.current.setClickEvent(clickEvent);
        return this;
    }

    public MinecraftTextBuilder hoverEvent(HoverEvent hoverEvent) {
        this.current.setHoverEvent(hoverEvent);
        return this;
    }

    public MinecraftTextBuilder append(String text) {
        return this.append(new MinecraftTextObject(text));
    }

    public MinecraftTextBuilder append(MinecraftTextObject textObject) {
        return this.appendJson(textObject); // This completes the object we're on. No turning back!
    }

    public MinecraftTextBuilder appendJson(MinecraftTextObject object) {
        if (root == null) {
            this.root = object;
            this.current = object;
        } else
            this.extra.add(this.current = object);

        return this;
    }

    @Override
    public MinecraftTextObject build() {
        this.root.setExtra(extra); // currently we're only adding the extras to the root.
        return this.root;
    }

    public static MinecraftTextBuilder of(MinecraftTextObject textObject) {
        return new MinecraftTextBuilder(textObject);
    }

    public static MinecraftTextBuilder of(String text) {
        return of(new MinecraftTextObject(text));
    }

    public static MinecraftTextBuilder empty() {
        return new MinecraftTextBuilder(null);
    }

}
