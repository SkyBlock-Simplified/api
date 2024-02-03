package dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.island.util;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class NbtContent {

    private int type; // Always 0
    @SerializedName("data")
    @Getter private String rawData;

    public byte[] getData() {
        return StringUtil.decodeBase64(this.getRawData().toCharArray());
    }

    public @NotNull CompoundTag getNbtData() throws NbtException {
        return SimplifiedApi.getNbtFactory().fromBase64(this.getRawData());
    }

}