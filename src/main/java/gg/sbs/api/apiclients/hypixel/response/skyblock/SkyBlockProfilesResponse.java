package gg.sbs.api.apiclients.hypixel.response.skyblock;

import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.Getter;

public class SkyBlockProfilesResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<SkyBlockIsland> islands;

}