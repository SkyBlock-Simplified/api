package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Triple;
import dev.sbs.api.util.helper.FormatUtil;

public final class RegisteredBuilderException extends SimplifiedException {

    private RegisteredBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Builder ''{0}'' is already registered!", tClass.getName());
    }

}
