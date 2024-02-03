package dev.sbs.api.manager.exception;

import org.jetbrains.annotations.NotNull;

public abstract class ManagerException extends RuntimeException {

    protected ManagerException(@NotNull String message) {
        super(message);
    }

}
