package dev.sbs.api.data.model.skyblock.location_data.location_remotes;

import dev.sbs.api.data.model.Model;

public interface LocationRemoteModel extends Model {

    String getKey();

    String getName();

    String getMode();

}