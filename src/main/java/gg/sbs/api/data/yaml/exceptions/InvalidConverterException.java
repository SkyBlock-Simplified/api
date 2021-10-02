package gg.sbs.api.data.yaml.exceptions;

@SuppressWarnings("unused")
public class InvalidConverterException extends YamlException {

	public InvalidConverterException() { }

	public InvalidConverterException(String message) {
		super(message);
	}

	public InvalidConverterException(Throwable cause) {
		super(cause);
	}

	public InvalidConverterException(String message, Throwable cause) {
		super(message, cause);
	}

}