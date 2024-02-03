package dev.sbs.api.minecraft.nbt.exception;

import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.mutable.triple.Triple;

/**
 * Checked exception thrown after reaching maximum depth deserializing an NBT tag.
 */
public class MaxDepthException extends NbtException {

    private MaxDepthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

}
