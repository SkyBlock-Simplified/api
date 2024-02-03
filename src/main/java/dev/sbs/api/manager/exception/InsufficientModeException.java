package dev.sbs.api.manager.exception;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.mutable.triple.Triple;
import dev.sbs.api.util.SimplifiedException;

public final class InsufficientModeException extends SimplifiedException {

    private InsufficientModeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Manager.Mode mode) {
        return String.format("Manager mode '%s' is insufficient to perform this action!", mode.name());
    }

}
