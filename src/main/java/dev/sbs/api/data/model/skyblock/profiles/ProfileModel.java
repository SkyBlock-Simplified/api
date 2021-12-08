package dev.sbs.api.data.model.skyblock.profiles;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockIsland;
import dev.sbs.api.data.model.Model;

public interface ProfileModel extends Model {

    String getKey();

    String getName();

    default SkyBlockIsland.ProfileName getProfileName() {
        return SkyBlockIsland.ProfileName.valueOf(this.getKey());
    }

}
