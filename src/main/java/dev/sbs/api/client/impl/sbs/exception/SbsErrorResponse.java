package dev.sbs.api.client.impl.sbs.exception;

import dev.sbs.api.client.exception.ApiErrorResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SbsErrorResponse implements ApiErrorResponse {

    protected boolean success;
    protected String error;
    protected String reason;

    public static class Unknown extends SbsErrorResponse {

        public Unknown() {
            super.success = false;
            super.error = "UNKNOWN";
            super.reason = "Unknown reason.";
        }

    }

}
