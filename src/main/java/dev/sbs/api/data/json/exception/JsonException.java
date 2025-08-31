package dev.sbs.api.data.json.exception;

import dev.sbs.api.data.exception.DataException;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsonException extends DataException {

    public JsonException(@NotNull Throwable cause) {
        super(cause);
    }

    public JsonException(@NotNull String message) {
        super(message);
    }

    public JsonException(@NotNull Throwable cause, @NotNull String message) {
        super(cause, message);
    }

    public JsonException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(message, args);
    }

    public JsonException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(cause, message, args);
    }

}
