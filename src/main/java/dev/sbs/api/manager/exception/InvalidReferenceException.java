package dev.sbs.api.manager.exception;

import org.jetbrains.annotations.NotNull;

public final class InvalidReferenceException extends ManagerException {

    public InvalidReferenceException(@NotNull Object identifier, @NotNull Object value) {
        super(String.format("Reference '%s' does not match instance '%s'!", identifier, value));
    }

}
