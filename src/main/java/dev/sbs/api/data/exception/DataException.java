package dev.sbs.api.data.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataException extends RuntimeException {

    public DataException(@NotNull Throwable cause) {
        super(cause);
    }

    public DataException(@NotNull String message) {
        super(message);
    }

    public DataException(@NotNull Throwable cause, @NotNull String message) {
        super(message, cause);
    }

    public DataException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args));
    }

    public DataException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args), cause);
    }

}
