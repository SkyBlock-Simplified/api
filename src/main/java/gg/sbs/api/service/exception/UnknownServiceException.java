package gg.sbs.api.service.exception;

import gg.sbs.api.util.FormatUtil;

public final class UnknownServiceException extends IllegalArgumentException {

	public UnknownServiceException(Class<?> service) {
		super(FormatUtil.format("Service ''{0}'' has not been registered", service.getName()));
	}

}