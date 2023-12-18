package dev.sbs.api.client.mojang.request;

import dev.sbs.api.client.mojang.exception.MojangApiException;
import dev.sbs.api.client.mojang.expander.StringArrayQuoteExpander;
import dev.sbs.api.client.mojang.response.MojangMultiUsernameResponse;
import dev.sbs.api.client.mojang.response.MojangUsernameResponse;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface MojangApiRequest extends IMojangRequest {

    default @NotNull MojangMultiUsernameResponse getMultipleUniqueIds(@NotNull Collection<String> usernames) throws MojangApiException {
        return this.getMultipleUniqueIds(usernames.toArray(new String[] { }));
    }

    @Headers("Content-Type: application/json")
    @RequestLine("POST /profiles/minecraft")
    @Body("[{usernames}]")
    @NotNull MojangMultiUsernameResponse getMultipleUniqueIds(@NotNull @Param(value = "usernames",  expander = StringArrayQuoteExpander.class) String... usernames) throws MojangApiException;

    @RequestLine("GET /users/profiles/minecraft/{username}")
    @NotNull MojangUsernameResponse getUniqueId(@NotNull @Param("username") String username) throws MojangApiException;

}
