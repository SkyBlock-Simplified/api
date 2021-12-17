package dev.sbs.api.manager.service.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidServiceException extends SimplifiedException {

    public static String getMessage(Class<?> tClass, Object instance) {
        return FormatUtil.format("Service ''{0}'' does not match instance ''{1}''!", tClass.getName(), instance);
    }

}
