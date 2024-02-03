package dev.sbs.api.manager.exception;

import dev.sbs.api.manager.Manager;
import org.jetbrains.annotations.NotNull;

public final class InsufficientModeException extends ManagerException {

    public InsufficientModeException(@NotNull Manager.Mode mode) {
        super(String.format("Manager mode '%s' is insufficient to perform this action!", mode.name()));
    }

}
