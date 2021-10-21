package dev.sbs.api.client.mojang.response;

import lombok.Getter;

public class MojangErrorResponse {

    private int code;
    private String error;
    @Getter private String reason;

}