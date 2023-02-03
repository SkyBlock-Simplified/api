package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.minecraft.nbt.exception.NbtException;
import dev.sbs.api.minecraft.nbt.tags.collection.CompoundTag;
import dev.sbs.api.util.helper.DataUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NbtContent {

    private int type; // Always 0
    @SerializedName("data")
    @Getter
    private String rawData;

    public byte[] getData() {
        return DataUtil.decode(this.getRawData().toCharArray());
    }

    public CompoundTag getNbtData() throws NbtException {
        return SimplifiedApi.getNbtFactory().fromBase64(this.getRawData());
    }

}