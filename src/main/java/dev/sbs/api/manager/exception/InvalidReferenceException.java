package dev.sbs.api.manager.exception;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.mutable.triple.Triple;
import dev.sbs.api.util.SimplifiedException;

public final class InvalidReferenceException extends SimplifiedException {

    private InvalidReferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Object identifier, Object value) {
        return String.format("Reference '%s' does not match instance '%s'!", identifier, value);
    }

}
