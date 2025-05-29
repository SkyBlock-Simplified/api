package dev.sbs.api.minecraft.exception;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * General minecraft exceptions.
 */
public class MinecraftException extends RuntimeException {

    public MinecraftException(@NotNull Throwable cause) {
        super(cause);
    }

    public MinecraftException(@NotNull String message) {
        super(message);
    }

    public MinecraftException(@NotNull Throwable cause, @NotNull String message) {
        super(message, cause);
    }

    public MinecraftException(@NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args));
    }

    public MinecraftException(@NotNull Throwable cause, @NotNull @PrintFormat String message, @Nullable Object... args) {
        super(String.format(message, args), cause);
    }

}
