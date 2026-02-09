package dev.sbs.api.client.exception;

import org.jetbrains.annotations.NotNull;

public interface ClientErrorResponse {

    @NotNull String getReason();

}
