package dev.sbs.api.data.model.skyblock.profiles;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockIsland;
import dev.sbs.api.data.model.Model;

public interface ProfileModel extends Model {

    SkyBlockIsland.ProfileName getKey();

    String getName();

}
