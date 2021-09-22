package gg.sbs.api.service;

public final class ServiceProvider<T> {

	private final Class<T> service;
	private final T provider;

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

	public T getProvider() {
		return this.provider;
	}

	public Class<T> getService() {
		return this.service;
	}

	@Override
	public int hashCode() {
		return this.getService().hashCode();
	}

}