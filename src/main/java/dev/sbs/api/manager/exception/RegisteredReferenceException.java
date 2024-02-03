package dev.sbs.api.manager.exception;

import org.jetbrains.annotations.NotNull;

public final class RegisteredReferenceException extends ManagerException {

    public RegisteredReferenceException(@NotNull Object identifier) {
        super(String.format("Reference '%s' is already registered!", identifier));
    }

}
