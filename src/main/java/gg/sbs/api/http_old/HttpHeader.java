package gg.sbs.api.http_old;

public class HttpHeader implements NameValuePair {

	private String name;
	private String value;

	public HttpHeader(String name, String value) {
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