package gg.sbs.api.apiclients.hypixel;

import gg.sbs.api.apiclients.ApiBuilder;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelDataInterface;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class HypixelApiBuilder extends ApiBuilder<HypixelDataInterface> {

    public HypixelApiBuilder() {
        super("api.hypixel.net");
    }

    public static final Pattern apiKeyRegex = Pattern.compile("[a-z0-9]{8}-(?:[a-z0-9]{4}-){3}[a-z0-9]{12}");
    @Getter private String apiKey;

    public void setApiKey(String newApiKey) {
        if (newApiKey == null)
            throw new IllegalArgumentException("New Hypixel API key must not be null");
        else if (apiKeyRegex.matcher(newApiKey).matches())
            throw new IllegalArgumentException("New Hypixel API key must be valid");

        this.apiKey = newApiKey;
    }

    public Map<String, String> buildHeaders() {
        return new HashMap<String, String>() {{
           put("API-Key", HypixelApiBuilder.this.getApiKey());
        }};
    }

}