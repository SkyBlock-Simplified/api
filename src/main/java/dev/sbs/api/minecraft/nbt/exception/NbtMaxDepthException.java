package dev.sbs.api.minecraft.nbt.exception;

/**
 * Checked exception thrown after reaching maximum depth deserializing an NBT tag.
 */
public class NbtMaxDepthException extends NbtException {

    public NbtMaxDepthException() {
        super("Maximum depth has been reached!");
    }

}
