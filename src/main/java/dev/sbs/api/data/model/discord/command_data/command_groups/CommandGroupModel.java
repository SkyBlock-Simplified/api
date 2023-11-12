package dev.sbs.api.data.model.discord.command_data.command_groups;

import dev.sbs.api.data.model.Model;

public interface CommandGroupModel extends Model {

    String getKey();

    String getName();

    String getDescription();

}
