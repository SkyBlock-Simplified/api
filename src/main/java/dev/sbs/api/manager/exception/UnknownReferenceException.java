package dev.sbs.api.manager.exception;

public final class UnknownReferenceException extends ManagerException {

    public UnknownReferenceException(Object identifier) {
        super(String.format("Reference '%s' has not been registered!", identifier));
    }

    public static String getMessage(Object identifier) {
        return String.format("Reference '%s' has not been registered!", identifier);
    }

}
