package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.util.helper.FormatUtil;

public final class RegisteredBuilderException extends UnsupportedOperationException {

	public RegisteredBuilderException(Class<?> service) {
		super(FormatUtil.format("Builder ''{0}'' is already registered", service.getName()));
	}

}
