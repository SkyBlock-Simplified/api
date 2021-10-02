package gg.sbs.api.data.yaml.exceptions;

@SuppressWarnings("serial")
public class InvalidConfigurationException extends YamlException {

	public InvalidConfigurationException() { }

	public InvalidConfigurationException(String message) {
		super(message);
	}

	public InvalidConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}