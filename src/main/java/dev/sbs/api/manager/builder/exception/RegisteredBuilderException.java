package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;

public final class RegisteredBuilderException extends SimplifiedException {

    RegisteredBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentMap<String, Object> fields) {
        super(message, cause, enableSuppression, writableStackTrace, fields);
    }

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Builder ''{0}'' is already registered!", tClass.getName());
    }

}
