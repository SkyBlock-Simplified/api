package gg.sbs.api.httpclients.mojang;

import com.google.gson.annotations.SerializedName;

public class MojangProfileResponse {
    private String name;

    @SerializedName("id")
    private String uuid;

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
