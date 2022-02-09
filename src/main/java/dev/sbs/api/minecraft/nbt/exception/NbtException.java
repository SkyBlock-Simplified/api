package dev.sbs.api.minecraft.nbt.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.tuple.Triple;

/**
 * {@link NbtException NbtExceptions} are thrown when the {@link NbtFactory} class is unable<br>
 * to parse nbt data.
 */
public final class NbtException extends SimplifiedException {

    private NbtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

}
