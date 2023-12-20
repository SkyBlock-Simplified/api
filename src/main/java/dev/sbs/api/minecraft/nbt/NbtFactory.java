package dev.sbs.api.minecraft.nbt;

import dev.sbs.api.minecraft.nbt.io.NbtReader;
import dev.sbs.api.minecraft.nbt.io.NbtWriter;
import dev.sbs.api.minecraft.nbt.serializable.snbt.SnbtConfig;
import dev.sbs.api.minecraft.nbt.tags.TagRegistry;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Standard interface for reading and writing NBT data structures.
 */
@Getter
@Setter
public class NbtFactory implements NbtReader, NbtWriter {

    private @NotNull TagRegistry registry;
    private @NotNull SnbtConfig snbtConfig;

    /**
     * Constructs an instance of this class using the default {@link TagRegistry} and {@link SnbtConfig}.
     */
    public NbtFactory() {
        this(new SnbtConfig());
    }

    /**
     * Constructs an instance of this class using the provided {@link TagRegistry} and default {@link SnbtConfig}.
     *
     * @param registry The TagRegistry to be used.
     */
    public NbtFactory(@NotNull TagRegistry registry) {
        this(registry, new SnbtConfig());
    }

    /**
     * Constructs an instance of this class using the default {@link TagRegistry} and provided {@link SnbtConfig}.
     *
     * @param snbtConfig The SNBT config to be used.
     */
    public NbtFactory(@NotNull SnbtConfig snbtConfig) {
        this(new TagRegistry(), snbtConfig);
    }

    /**
     * Constructs an instance of this class using the provided {@link TagRegistry} and {@link SnbtConfig}.
     *
     * @param registry The TagRegistry to be used.
     * @param snbtConfig The SNBT config to be used.
     */
    public NbtFactory(@NotNull TagRegistry registry, @NotNull SnbtConfig snbtConfig) {
        this.registry = registry;
        this.snbtConfig = snbtConfig;
    }

}
