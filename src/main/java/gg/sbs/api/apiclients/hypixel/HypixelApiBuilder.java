package gg.sbs.api.apiclients.hypixel;

import com.google.common.base.Preconditions;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import gg.sbs.api.apiclients.ApiBuilder;
import gg.sbs.api.apiclients.exception.HypixelApiException;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelDataInterface;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class HypixelApiBuilder extends ApiBuilder<HypixelDataInterface> {

    public static final Pattern apiKeyRegex = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");
    @Getter private String apiKey;

    public HypixelApiBuilder() {
        super("api.hypixel.net");
    }

    public void setApiKey(String apiKey) {
        Preconditions.checkNotNull(apiKey, "Hypixel API key must not be NULL");
        Preconditions.checkArgument(apiKeyRegex.matcher(apiKey).matches(), "Hypixel API key must be valid");
        this.apiKey = apiKey;
    }

    @Override
    public Map<String, String> buildHeaders() {
        return new HashMap<String, String>() {{
           put("API-Key", HypixelApiBuilder.this.getApiKey());
        }};
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}