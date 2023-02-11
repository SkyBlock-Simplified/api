package dev.sbs.api.data.model.discord.command_data.command_parents;

import dev.sbs.api.data.model.Model;

public interface CommandParentModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    boolean isPrefix();

    default boolean notPrefix() {
        return !this.isPrefix();
    }

}
