package gg.sbs.api.service;

import lombok.Getter;

public final class ServiceProvider<T> {

	@Getter private final Class<T> service;
	@Getter private final T provider;

	ServiceProvider(Class<T> service, T provider) {
		this.service = service;
		this.provider = provider;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (obj == this)
			return true;
		else if (obj instanceof ServiceProvider) {
			ServiceProvider provider = (ServiceProvider)obj;
			return provider.getService().equals(this.getService());
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return this.getService().hashCode();
	}

}