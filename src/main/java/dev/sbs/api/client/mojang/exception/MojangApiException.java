package dev.sbs.api.client.mojang.exception;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.ApiException;
import dev.sbs.api.util.helper.StringUtil;
import feign.FeignException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@Getter
public final class MojangApiException extends ApiException {

    private final @NotNull MojangErrorResponse errorResponse;

    public MojangApiException(FeignException exception) {
        super(exception);

        if (this.responseBody().isPresent()) {
            String bodyString = StringUtil.toEncodedString(this.responseBody().get().array(), StandardCharsets.UTF_8);
            this.errorResponse = SimplifiedApi.getGson().fromJson(bodyString, MojangErrorResponse.class);
        } else
            this.errorResponse = new MojangErrorResponse.Unknown();
    }

}
