package gg.sbs.api.service.exceptions;

import gg.sbs.api.util.FormatUtil;

public final class RegisteredServiceException extends UnsupportedOperationException {

	public RegisteredServiceException(Class<?> service) {
		super(FormatUtil.format("Service ''{0}'' already registered!", service.getName()));
	}

}