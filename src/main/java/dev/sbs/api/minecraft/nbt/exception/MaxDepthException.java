package dev.sbs.api.minecraft.nbt.exception;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.mutable.triple.Triple;

/**
 * Checked exception thrown after reaching maximum depth deserializing an NBT tag.
 */
public class MaxDepthException extends NbtException {

    private MaxDepthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

}
