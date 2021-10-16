package gg.sbs.api.manager.service.exception;

import gg.sbs.api.util.helper.FormatUtil;

public final class RegisteredServiceException extends UnsupportedOperationException {

	public RegisteredServiceException(Class<?> service) {
		super(FormatUtil.format("Service ''{0}'' is already registered", service.getName()));
	}

}