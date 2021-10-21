package dev.sbs.api.minecraft.text;

import dev.sbs.api.minecraft.text.event.ClickEvent;
import dev.sbs.api.minecraft.text.event.HoverEvent;
import dev.sbs.api.util.builder.Builder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;

public final class MinecraftTextBuilder implements Builder<MinecraftTextObject> {
    
    private MinecraftTextObject root;
    private MinecraftTextObject current; // The current text object. This will change when we append text for example
    private final ConcurrentList<MinecraftTextObject> extra = Concurrent.newList(); // The storage of the extra items

    private MinecraftTextBuilder(MinecraftTextObject root) {
        this.root = root;
        this.current = root;
    }

    public MinecraftTextBuilder color(ChatColor color) {
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
        // essentially this completes what ever object we were on. No turning back!
        return this.appendJson(new MinecraftTextObject(text));
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
        // currently we're only adding the extras to the root.
        this.root.setExtra(extra);
        return this.root;
    }

    public static MinecraftTextBuilder of(String text) {
        return new MinecraftTextBuilder(new MinecraftTextObject(text));
    }

    public static MinecraftTextBuilder empty() {
        return new MinecraftTextBuilder(null);
    }

}