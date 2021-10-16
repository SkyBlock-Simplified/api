package gg.sbs.api.manager.builder.exception;

import gg.sbs.api.util.helper.FormatUtil;

public final class InvalidBuilderException extends IllegalArgumentException {

	public InvalidBuilderException(Class<?> service, Object instance) {
		super(FormatUtil.format("Builder ''{0}'' does not build instances of ''{1}''", service.getName(), instance));
	}

}