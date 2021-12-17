package dev.sbs.api.manager.service.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.helper.FormatUtil;

public final class RegisteredServiceException extends SimplifiedException {

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Service ''{0}'' is already registered!", tClass.getName());
    }

}
