package gg.sbs.api.manager.service.exception;

import gg.sbs.api.util.helper.FormatUtil;

public final class UnknownServiceException extends IllegalArgumentException {

	public UnknownServiceException(Class<?> service) {
		super(FormatUtil.format("Service ''{0}'' has not been registered", service.getName()));
	}

}