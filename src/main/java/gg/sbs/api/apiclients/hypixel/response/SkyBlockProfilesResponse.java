package gg.sbs.api.apiclients.hypixel.response;

import gg.sbs.api.apiclients.hypixel.SkyBlockIsland;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.Getter;

public class SkyBlockProfilesResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<SkyBlockIsland> islands;

}