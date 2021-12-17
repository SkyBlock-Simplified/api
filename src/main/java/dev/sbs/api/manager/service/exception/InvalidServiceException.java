package dev.sbs.api.manager.service.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidServiceException extends SimplifiedException {

    InvalidServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentMap<String, Object> fields) {
        super(message, cause, enableSuppression, writableStackTrace, fields);
    }

    public static String getMessage(Class<?> tClass, Object instance) {
        return FormatUtil.format("Service ''{0}'' does not match instance ''{1}''!", tClass.getName(), instance);
    }

}
