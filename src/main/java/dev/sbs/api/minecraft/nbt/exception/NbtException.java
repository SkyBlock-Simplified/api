package dev.sbs.api.minecraft.nbt.exception;

import dev.sbs.api.minecraft.exception.MinecraftException;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import org.jetbrains.annotations.NotNull;

/**
 * {@link NbtException NbtExceptions} are thrown when the {@link NbtFactory} class is unable<br>
 * to parse nbt data.
 */
public class NbtException extends MinecraftException {

    public NbtException(@NotNull Exception exception) {
        super(exception);
    }

    public NbtException(@NotNull String message) {
        super(message);
    }

}
