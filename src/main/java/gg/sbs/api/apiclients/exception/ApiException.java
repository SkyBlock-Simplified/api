package gg.sbs.api.apiclients.exception;

import feign.FeignException;
import gg.sbs.api.apiclients.HttpStatus;
import lombok.Getter;

public abstract class ApiException extends FeignException {

    @Getter private final HttpStatus httpStatus;

    public ApiException(FeignException exception) {
        super(exception.status(), exception.getMessage(), exception.getCause(), exception.content(), exception.responseHeaders());
        this.httpStatus = HttpStatus.getByCode(this.status());
    }

}