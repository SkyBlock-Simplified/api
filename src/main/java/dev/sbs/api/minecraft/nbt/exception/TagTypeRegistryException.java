package dev.sbs.api.minecraft.nbt.exception;

import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.mutable.tuple.triple.Triple;

/**
 * Checked exception thrown when any issue arises relating to the {@link TagRegistry}.
 */
public class TagTypeRegistryException extends NbtException {

    private TagTypeRegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

}
