package gg.sbs.api.data.yaml.exceptions;

public class YamlException extends RuntimeException {

    private static final long serialVersionUID = -4738336175050337570L;

    public YamlException() { }

    public YamlException(String message) {
        super(message);
    }

    public YamlException(Throwable cause) {
        super(cause);
    }

    public YamlException(String message, Throwable cause) {
        super(message, cause);
    }

}