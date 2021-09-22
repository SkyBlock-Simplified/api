package gg.sbs.api.service.exceptions;

import gg.sbs.api.util.StringUtil;

public final class RegisteredServiceException extends UnsupportedOperationException {

	public RegisteredServiceException(Class<?> service) {
		super(StringUtil.format("Service ''{0}'' already registered!", service.getName()));
	}

}