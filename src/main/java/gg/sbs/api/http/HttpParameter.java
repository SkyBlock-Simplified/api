package gg.sbs.api.http;

public class HttpParameter implements NameValuePair {

	private String name;
	private String value;

	public HttpParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}