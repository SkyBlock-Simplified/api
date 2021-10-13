package gg.sbs.api.manager.builder;

import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.builder.CoreBuilder;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;

public final class BuilderProvider<T, B extends CoreBuilder> {

	@Getter private final Class<T> service;
	@Getter private final Class<B> builder;

	BuilderProvider(Class<T> service, Class<B> builder) {
		this.service = service;
		this.builder = builder;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (obj == this)
			return true;
		else {
			BuilderProvider provider = (BuilderProvider)obj;
			return provider.getBuilder().equals(this.getBuilder());
		}
	}

	@SuppressWarnings("unchecked")
	public B createBuilder() {
		return (B)new Reflection(this.getBuilder()).newInstance();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.service).append(this.builder).build();
	}

}