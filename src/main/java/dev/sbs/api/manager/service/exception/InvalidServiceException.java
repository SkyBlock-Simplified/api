package dev.sbs.api.manager.service.exception;

import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidServiceException extends IllegalArgumentException {

    public InvalidServiceException(Class<?> service, Object instance) {
        super(FormatUtil.format("Service ''{0}'' does not match instance ''{1}''", service.getName(), instance));
    }

}
