package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.manager.builder.BuilderProvider;
import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidBuilderException extends SimplifiedException {

    public static String getMessage(Class<?> tClass, BuilderProvider provider) {
        return getMessage(tClass, provider.getBuilder());
    }

    public static String getMessage(Class<?> tClass, Class<?> bClass) {
        return FormatUtil.format("Builder ''{0}'' does not build instances of ''{1}''!", tClass.getName(), bClass.getName());

    }

}
