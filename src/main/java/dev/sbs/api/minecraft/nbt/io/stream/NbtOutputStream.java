package dev.sbs.api.minecraft.nbt.io.stream;

import dev.sbs.api.minecraft.nbt.io.NbtOutput;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.minecraft.nbt.tags.TagType;
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
import dev.sbs.api.util.helper.PrimitiveUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Implementation for NBT serialization.
 */
public class NbtOutputStream extends DataOutputStream implements NbtOutput {

    public NbtOutputStream(@NotNull OutputStream outputStream) {
        super(outputStream);
    }

    @SneakyThrows
    @Override
    public void writeByteTag(@NotNull ByteTag tag) {
        this.writeByte(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeShortTag(@NotNull ShortTag tag) {
        this.writeShort(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeIntTag(@NotNull IntTag tag) {
        this.writeInt(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeLongTag(@NotNull LongTag tag) {
        this.writeLong(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeFloatTag(@NotNull FloatTag tag) {
        this.writeFloat(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeDoubleTag(@NotNull DoubleTag tag) {
        this.writeDouble(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeByteArrayTag(@NotNull ByteArrayTag tag) {
        this.writeInt(tag.length());
        this.write(PrimitiveUtil.unwrap(tag.getValue()));
    }

    @SneakyThrows
    @Override
    public void writeStringTag(@NotNull StringTag tag) {
        this.writeUTF(tag.getValue());
    }

    @SneakyThrows
    @Override
    public void writeListTag(@NotNull ListTag<Tag<?>> tag, int maxDepth) {
        this.writeByte(tag.getListType());
        this.writeInt(tag.size());

        for (Tag<?> element : tag)
            this.writeTag(element, this.decrementMaxDepth(maxDepth));
    }

    @SneakyThrows
    @Override
    public void writeCompoundTag(@NotNull CompoundTag tag, int maxDepth) {
        for (Map.Entry<String, Tag<?>> entry : tag) {
            if (entry.getValue().getId() == TagType.END.getId())
                break;

            this.writeByte(entry.getValue().getId());
            this.writeUTF(StringUtil.stripToEmpty(entry.getKey()));
            this.writeTag(entry.getValue(), this.decrementMaxDepth(maxDepth));
        }

        this.writeByte(0);
    }

    @SneakyThrows
    @Override
    public void writeIntArrayTag(@NotNull IntArrayTag tag) {
        this.writeInt(tag.length());

        for (int i : tag.getValue())
            this.writeInt(i);
    }

    @SneakyThrows
    @Override
    public void writeLongArrayTag(@NotNull LongArrayTag tag) {
        this.writeInt(tag.length());

        for (long i : tag.getValue())
            this.writeLong(i);
    }

}
