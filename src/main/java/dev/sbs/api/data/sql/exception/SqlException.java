package dev.sbs.api.data.sql.exception;

import dev.sbs.api.data.exception.DataException;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SqlException extends DataException {

    public SqlException(@NotNull Throwable cause) {
        super(cause);
    }

    public SqlException(@NotNull String message) {
        super(message);
    }

    public SqlException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args));
    }

}
