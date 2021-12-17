package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.manager.builder.BuilderProvider;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidBuilderException extends SimplifiedException {

    InvalidBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentMap<String, Object> fields) {
        super(message, cause, enableSuppression, writableStackTrace, fields);
    }

    public static String getMessage(Class<?> tClass, BuilderProvider provider) {
        return getMessage(tClass, provider.getBuilder());
    }

    public static String getMessage(Class<?> tClass, Class<?> bClass) {
        return FormatUtil.format("Builder ''{0}'' does not build instances of ''{1}''!", tClass.getName(), bClass.getName());

    }

}
