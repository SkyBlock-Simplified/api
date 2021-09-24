package gg.sbs.api.nbt.snbt;

import gg.sbs.api.nbt.registry.TagTypeRegistry;

/**
 * Interface for SNBT serialization. Must be implemented if your tag will be SNBT serializable. Reading is not yet supported.
 */
public interface SnbtSerializable {

    String toSnbt(int depth, TagTypeRegistry registry, SnbtConfig config);

}
