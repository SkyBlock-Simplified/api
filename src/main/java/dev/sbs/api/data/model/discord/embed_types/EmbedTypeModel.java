package dev.sbs.api.data.model.discord.embed_types;

import dev.sbs.api.data.model.Model;

public interface EmbedTypeModel extends Model {

    String getKey();

    String getName();

    String getDescription();

}
