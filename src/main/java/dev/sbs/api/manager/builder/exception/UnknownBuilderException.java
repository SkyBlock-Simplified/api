package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Triple;
import dev.sbs.api.util.helper.FormatUtil;

public final class UnknownBuilderException extends SimplifiedException {

    private UnknownBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Builder ''{0}'' has not been registered!", tClass.getName());
    }

}
