package gg.sbs.api.client.hypixel;

import com.google.common.base.Preconditions;
import feign.FeignException;
import feign.codec.ErrorDecoder;
import gg.sbs.api.client.ApiBuilder;
import gg.sbs.api.client.exception.HypixelApiException;
import gg.sbs.api.client.hypixel.implementation.HypixelRequestInterface;
import gg.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public final class HypixelApiBuilder extends ApiBuilder<HypixelRequestInterface> {

    public static final Pattern apiKeyRegex = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");
    @Getter private UUID apiKey;

    public HypixelApiBuilder() {
        super("api.hypixel.net");
    }

    public void setApiKey(String apiKey) {
        Preconditions.checkNotNull(apiKey, "Hypixel API key must not be NULL");
        Preconditions.checkArgument(apiKeyRegex.matcher(apiKey).matches(), "Hypixel API key must be valid");
        this.apiKey = StringUtil.toUUID(apiKey);
    }

    @Override
    public Map<String, String> buildHeaders() {
        return new HashMap<String, String>() {{
           put("API-Key", HypixelApiBuilder.this.getApiKey().toString());
        }};
    }

    @Override
    public ErrorDecoder getErrorDecoder() {
        return (methodKey, response) -> {
            throw new HypixelApiException(FeignException.errorStatus(methodKey, response));
        };
    }

}