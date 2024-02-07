package dev.sbs.api.minecraft.nbt.io.json;

import com.google.gson.stream.JsonWriter;
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
import dev.sbs.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Implementation for NBT JSON serialization.
 */
public class NbtJsonSerializer extends JsonWriter implements NbtOutput {

    public NbtJsonSerializer(@NotNull Writer writer) {
        super(writer);
        this.setIndent("    ");
    }

    @Override
    public void writeByteTag(@NotNull ByteTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeShortTag(@NotNull ShortTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeIntTag(@NotNull IntTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeLongTag(@NotNull LongTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeFloatTag(@NotNull FloatTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeDoubleTag(@NotNull DoubleTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeByteArrayTag(@NotNull ByteArrayTag tag) throws IOException {
        this.beginArray();

        for (byte b : tag)
            this.value(b);

        this.endArray();
    }

    @Override
    public void writeStringTag(@NotNull StringTag tag) throws IOException {
        this.value(tag.getValue());
    }

    @Override
    public void writeListTag(@NotNull ListTag<Tag<?>> tag, int depth) throws IOException {
        this.beginArray();

        for (Tag<?> element : tag)
            this.writeTag(element, this.incrementMaxDepth(depth));

        this.endArray();
    }

    @Override
    public void writeCompoundTag(@NotNull CompoundTag tag, int depth) throws IOException {
        this.beginObject();

        for (Map.Entry<String, Tag<?>> entry : tag) {
            if (entry.getValue().getId() == TagType.END.getId())
                break;

            this.name(StringUtil.stripToEmpty(entry.getKey()));
            this.writeTag(entry.getValue(), this.incrementMaxDepth(depth));
        }

        this.endObject();
    }

    @Override
    public void writeIntArrayTag(@NotNull IntArrayTag tag) throws IOException {
        this.beginArray();

        for (int b : tag)
            this.value(b);

        this.endArray();
    }

    @Override
    public void writeLongArrayTag(@NotNull LongArrayTag tag) throws IOException {
        this.beginArray();

        for (long b : tag)
            this.value(b);

        this.endArray();
    }

}
