package dev.sbs.api.client.sbs.exception;

import lombok.Getter;

public class SbsErrorResponse {

    private int code;
    private String error;
    @Getter protected String reason;

    public static class Unknown extends SbsErrorResponse {

        public Unknown() {
            super.reason = "Unknown";
        }

    }

}
