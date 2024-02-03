package dev.sbs.api.minecraft.nbt.io.stream;

import dev.sbs.api.minecraft.nbt.io.NbtInput;
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
import dev.sbs.api.util.PrimitiveUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * Implementation for NBT deserialization.
 */
public class NbtInputStream extends DataInputStream implements NbtInput {

    public NbtInputStream(@NotNull InputStream inputStream) {
        super(inputStream);
    }

    @SneakyThrows
    @Override
    public @NotNull ByteTag readByteTag() {
        return new ByteTag(this.readByte());
    }

    @SneakyThrows
    @Override
    public @NotNull ShortTag readShortTag() {
        return new ShortTag(this.readShort());
    }

    @SneakyThrows
    @Override
    public @NotNull IntTag readIntTag() {
        return new IntTag(this.readInt());
    }

    @SneakyThrows
    @Override
    public @NotNull LongTag readLongTag() {
        return new LongTag(this.readLong());
    }

    @SneakyThrows
    @Override
    public @NotNull FloatTag readFloatTag() {
        return new FloatTag(this.readFloat());
    }

    @SneakyThrows
    @Override
    public @NotNull DoubleTag readDoubleTag() {
        return new DoubleTag(this.readDouble());
    }

    @SneakyThrows
    @Override
    public @NotNull ByteArrayTag readByteArrayTag() {
        byte[] tmp = new byte[this.readInt()];
        this.readFully(tmp);
        return new ByteArrayTag(PrimitiveUtil.wrap(tmp));
    }

    @SneakyThrows
    @Override
    public @NotNull StringTag readStringTag() {
        return new StringTag(this.readUTF());
    }

    @SneakyThrows
    @Override
    public @NotNull ListTag<?> readListTag(int maxDepth) {
        ListTag<Tag<?>> listTag = new ListTag<>();
        int listType = this.readUnsignedByte();
        int length = Math.max(0, this.readInt());

        for (int i = 0; i < length; i++)
            listTag.add(this.readTag((byte) listType, this.decrementMaxDepth(maxDepth)));

        return listTag;
    }

    @SneakyThrows
    @Override
    public @NotNull CompoundTag readCompoundTag(int maxDepth) {
        CompoundTag compoundTag = new CompoundTag();

        for (int id = this.readUnsignedByte() & 0xFF; id != 0; id = this.readUnsignedByte() & 0xFF) {
            String key = this.readUTF();
            Tag<?> tag = this.readTag((byte) id, this.decrementMaxDepth(maxDepth));
            compoundTag.put(key, tag);
        }

        return compoundTag;
    }

    @SneakyThrows
    @Override
    public @NotNull IntArrayTag readIntArrayTag() {
        int length = this.readInt();
        Integer[] data = new Integer[length];

        for (int i = 0; i < length; i++)
            data[i] = this.readInt();

        return new IntArrayTag(data);
    }

    @SneakyThrows
    @Override
    public @NotNull LongArrayTag readLongArrayTag() {
        int length = this.readInt();
        Long[] data = new Long[length];

        for (int i = 0; i < length; i++)
            data[i] = this.readLong();

        return new LongArrayTag(data);
    }

}
