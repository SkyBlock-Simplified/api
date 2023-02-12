package dev.sbs.api.client.discord.request;

import feign.Body;
import feign.Headers;
import feign.RequestLine;

public interface DiscordOAuthRequest extends DiscordRequestInterface {

    // TODO: https://discord.com/developers/docs/resources/application-role-connection-metadata
    // TODO: https://discord.com/developers/docs/tutorials/configuring-app-metadata-for-linked-roles

    @RequestLine("GET /oauth2/authorize")
    @Headers("Content-Type: application/json")
    @Body("{''}")
    void getAuthUrl();

    @RequestLine("POST /oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("\\{'client_id':{clientId},'client_secret':{clientSecret},'grant_type':'authorization_code',{code},'redirect_uri':{redirectUrl}\\}")
    void getToken();

}
