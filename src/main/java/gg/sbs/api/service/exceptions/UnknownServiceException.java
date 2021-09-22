package gg.sbs.api.service.exceptions;

import gg.sbs.api.util.StringUtil;

public final class UnknownServiceException extends IllegalArgumentException {

	public UnknownServiceException(Class<?> service) {
		super(StringUtil.format("Service ''{0}'' has not been registered!", service.getName()));
	}

}