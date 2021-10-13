package gg.sbs.api.apiclients.hypixel.response.hypixel;

import lombok.Getter;

public class HypixelErrorResponse {

    @Getter private boolean success;
    @Getter private String cause;
    @Getter private boolean throttle;
    @Getter private boolean global;

}