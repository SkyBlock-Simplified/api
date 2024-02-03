package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.array.ByteArrayTag;
import dev.sbs.api.minecraft.nbt.tags.array.IntArrayTag;
import dev.sbs.api.minecraft.nbt.tags.array.LongArrayTag;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.minecraft.nbt.tags.collection.ListTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.ByteTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.DoubleTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.FloatTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.IntTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.LongTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.ShortTag;
import dev.sbs.api.minecraft.nbt.tags.primitive.StringTag;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface NbtOutput extends MaxDepthIO {

    @SuppressWarnings("all")
    default void writeTag(@NotNull Tag<?> tag, int maxDepth) throws IOException {
        switch (tag.getId()) {
            case 1 -> this.writeByteTag((ByteTag) tag);
            case 2 -> this.writeShortTag((ShortTag) tag);
            case 3 -> this.writeIntTag((IntTag) tag);
            case 4 -> this.writeLongTag((LongTag) tag);
            case 5 -> this.writeFloatTag((FloatTag) tag);
            case 6 -> this.writeDoubleTag((DoubleTag) tag);
            case 7 -> this.writeByteArrayTag((ByteArrayTag) tag);
            case 8 -> this.writeStringTag((StringTag) tag);
            case 9 -> this.writeListTag((ListTag<Tag<?>>) tag, maxDepth);
            case 10 -> this.writeCompoundTag((CompoundTag) tag, maxDepth);
            case 11 -> this.writeIntArrayTag((IntArrayTag) tag);
            case 12 -> this.writeLongArrayTag((LongArrayTag) tag);
            default -> throw new UnsupportedOperationException(String.format("Tag with id %s is not supported.", tag.getId()));
        };
    }

    void writeByteTag(@NotNull ByteTag tag) throws IOException;

    void writeShortTag(@NotNull ShortTag tag) throws IOException;

    void writeIntTag(@NotNull IntTag tag) throws IOException;

    void writeLongTag(@NotNull LongTag tag) throws IOException;

    void writeFloatTag(@NotNull FloatTag tag) throws IOException;

    void writeDoubleTag(@NotNull DoubleTag tag) throws IOException;

    void writeByteArrayTag(@NotNull ByteArrayTag tag) throws IOException;

    void writeStringTag(@NotNull StringTag tag) throws IOException;

    public void writeListTag(@NotNull ListTag<Tag<?>> tag, int maxDepth) throws IOException;

    void writeCompoundTag(@NotNull CompoundTag tag, int maxDepth) throws IOException;

    void writeIntArrayTag(@NotNull IntArrayTag tag) throws IOException;

    void writeLongArrayTag(@NotNull LongArrayTag tag) throws IOException;

}
