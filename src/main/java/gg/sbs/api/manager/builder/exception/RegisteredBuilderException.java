package gg.sbs.api.manager.builder.exception;

import gg.sbs.api.util.FormatUtil;

public final class RegisteredBuilderException extends UnsupportedOperationException {

	public RegisteredBuilderException(Class<?> service) {
		super(FormatUtil.format("Builder ''{0}'' is already registered", service.getName()));
	}

}