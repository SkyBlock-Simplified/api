package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.minecraft.nbt.exception.NbtMaxDepthException;

public interface MaxDepthIO {

    default int decrementMaxDepth(int depth) {
        if (depth < 0)
            throw new IllegalArgumentException("Negative maximum depth is not allowed!");

        if (depth == 0) {
            throw new NbtMaxDepthException();
        }

        return --depth;
    }

}
