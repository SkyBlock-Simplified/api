package dev.sbs.api.manager.service.exception;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.tuple.Triple;

public final class UnknownServiceException extends SimplifiedException {

    private UnknownServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ConcurrentList<Triple<String, String, Boolean>> fields, ConcurrentMap<String, Object> data) {
        super(message, cause, enableSuppression, writableStackTrace, fields, data);
    }

    public static String getMessage(Class<?> tClass) {
        return FormatUtil.format("Service ''{0}'' has not been registered!", tClass.getName());
    }

}
