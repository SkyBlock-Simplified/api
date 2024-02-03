package dev.sbs.api.minecraft.nbt.io;

import dev.sbs.api.minecraft.nbt.exception.MaxDepthException;
import dev.sbs.api.util.SimplifiedException;

public interface MaxDepthIO {

    default int decrementMaxDepth(int depth) {
        if (depth < 0)
            throw new IllegalArgumentException("Negative maximum depth is not allowed!");

        if (depth == 0) {
            throw SimplifiedException.of(MaxDepthException.class)
                .withMessage("Maximum depth has been reached!")
                .build();
        }

        return --depth;
    }

}
