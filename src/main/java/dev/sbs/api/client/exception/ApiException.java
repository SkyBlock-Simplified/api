package dev.sbs.api.client.exception;

import dev.sbs.api.client.HttpStatus;
import feign.FeignException;
import lombok.Getter;

public abstract class ApiException extends FeignException {

    @Getter
    private final HttpStatus httpStatus;

    @SuppressWarnings("deprecation")
    public ApiException(FeignException exception) {
        super(exception.status(), exception.getMessage(), exception.getCause(), exception.content(), exception.responseHeaders());
        this.httpStatus = HttpStatus.getByCode(this.status());
    }

}
