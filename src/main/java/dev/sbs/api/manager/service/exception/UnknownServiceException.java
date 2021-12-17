package dev.sbs.api.manager.service.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.helper.FormatUtil;

public final class UnknownServiceException extends SimplifiedException {

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Service ''{0}'' has not been registered!", tClass.getName());
    }

}
