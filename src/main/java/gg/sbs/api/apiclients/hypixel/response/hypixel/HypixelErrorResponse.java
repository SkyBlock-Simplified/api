package gg.sbs.api.apiclients.hypixel.response.hypixel;

import lombok.Getter;

public class HypixelErrorResponse {

    @Getter private boolean success;
    @SuppressWarnings("cause")
    @Getter private String reason;
    @Getter private boolean throttle;
    @Getter private boolean global;

}