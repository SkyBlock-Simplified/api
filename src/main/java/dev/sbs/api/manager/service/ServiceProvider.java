package dev.sbs.api.manager.service;

import dev.sbs.api.manager.Provider;
import lombok.Getter;

public final class ServiceProvider extends Provider {

	@Getter private final Object provider;

	ServiceProvider(Class<?> service, Object provider) {
		super(service);
		this.provider = provider;
	}

}
