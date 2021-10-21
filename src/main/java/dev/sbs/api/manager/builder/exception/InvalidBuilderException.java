package dev.sbs.api.manager.builder.exception;

import dev.sbs.api.util.helper.FormatUtil;

public final class InvalidBuilderException extends IllegalArgumentException {

	public InvalidBuilderException(Class<?> service, Class<?> builder) {
		super(FormatUtil.format("Builder ''{0}'' does not build instances of ''{1}''", builder.getName(), service.getName()));
	}

}