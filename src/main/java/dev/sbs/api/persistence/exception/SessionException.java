package dev.sbs.api.persistence.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SessionException extends RuntimeException {

    public SessionException(@NotNull Throwable cause) {
        super(cause);
    }

    public SessionException(@NotNull String message) {
        super(message);
    }

    public SessionException(@NotNull Throwable cause, @NotNull String message) {
        super(message, cause);
    }

    public SessionException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args));
    }

    public SessionException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args), cause);
    }

}
