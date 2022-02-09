package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.manager.builder.BuilderProvider;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.tuple.Triple;

public final class InvalidBuilderException extends SimplifiedException {

    private InvalidBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Class<?> tClass, BuilderProvider provider) {
        return getMessage(tClass, provider.getBuilder());
    }

    public static String getMessage(Class<?> tClass, Class<?> bClass) {
        return FormatUtil.format("Builder ''{0}'' does not build instances of ''{1}''!", tClass.getName(), bClass.getName());

    }

}
