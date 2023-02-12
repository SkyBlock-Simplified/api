package dev.sbs.api.client.discord;

import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.discord.exception.DiscordApiException;
import dev.sbs.api.client.discord.request.DiscordRequestInterface;
import feign.FeignException;
import feign.codec.ErrorDecoder;

public final class DiscordApiBuilder extends ApiBuilder<DiscordRequestInterface> {

    public DiscordApiBuilder() {
        super("discord.com/api/v10");
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new DiscordApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}
