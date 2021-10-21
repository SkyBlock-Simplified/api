package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.util.helper.FormatUtil;

public final class UnknownBuilderException extends IllegalArgumentException {

	public UnknownBuilderException(Class<?> service) {
		super(FormatUtil.format("Builder ''{0}'' has not been registered", service.getName()));
	}

}