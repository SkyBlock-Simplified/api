package gg.sbs.api.manager.builder;

import gg.sbs.api.manager.Provider;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.builder.CoreBuilder;
import lombok.Getter;

public final class BuilderProvider extends Provider {

	@Getter private final Class<? extends CoreBuilder> builder;

	BuilderProvider(Class<?> serviceClass, Class<? extends CoreBuilder> builder) {
		super(serviceClass);
		this.builder = builder;
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance() {
		return (T) new Reflection(this.getBuilder()).newInstance();
	}

}