package dev.sbs.api.minecraft.nbt.snbt;

import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;

/**
 * Interface for SNBT serialization. Must be implemented if your tag will be SNBT serializable. Reading is not yet supported.
 */
public interface SnbtSerializable {

    default String toSnbt(TagTypeRegistry registry, SnbtConfig config) {
        return this.toSnbt(0, registry, config);
    }

    String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config);

}
