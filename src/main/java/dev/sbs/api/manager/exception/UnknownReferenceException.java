package dev.sbs.api.manager.exception;

import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Triple;
import dev.sbs.api.util.helper.FormatUtil;

public final class UnknownReferenceException extends SimplifiedException {

    private UnknownReferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Object identifier) {
        return FormatUtil.format("Reference ''{0}'' has not been registered!", identifier);
    }

}