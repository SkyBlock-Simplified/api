package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.helper.FormatUtil;

public final class UnknownBuilderException extends SimplifiedException {

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Builder ''{0}'' has not been registered!", tClass.getName());
    }

}
