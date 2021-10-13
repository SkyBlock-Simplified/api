package gg.sbs.api.manager.service.exception;

import gg.sbs.api.util.FormatUtil;

public final class InvalidServiceException extends IllegalArgumentException {

	public InvalidServiceException(Class<?> service, Object instance) {
		super(FormatUtil.format("Service ''{0}'' does not match instance ''{1}''", service.getName(), instance));
	}

}