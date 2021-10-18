package gg.sbs.api.client.mojang.response;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.client.HttpStatus;
import gg.sbs.api.client.mojang.MojangServices;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import lombok.Getter;

import java.net.URL;

public class MojangStatusResponse {

    @Getter private boolean success;
    private int httpCode;
    private ConcurrentMap<Service, State> status;

    public ConcurrentMap<Service, State> getAllStatus() {
        return this.status;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.getByCode(this.httpCode);
    }

    public State getState(Service service) {
        return this.status.get(service);
    }

    public enum Service {

        @SerializedName("minecraft.net")
        MINECRAFT(MojangServices.SERVICE_MINECRAFT),
        @SerializedName("session.minecraft.net")
        SESSION(MojangServices.SERVICE_MINECRAFT_SESSION),
        @SerializedName("account.minecraft.net")
        ACCOUNT(MojangServices.SERVICE_MOJANG_ACCOUNT),
        @SerializedName("authserver.minecraft.net")
        AUTHENTICATION_SERVER(MojangServices.SERVICE_MOJANG_AUTHSERVER),
        @SerializedName("sessionserver.minecraft.net")
        SESSION_SERVER(MojangServices.SERVICE_MOJANG_SESSION),
        @SerializedName("api.minecraft.net")
        API(MojangServices.SERVICE_MOJANG_API),
        @SerializedName("textures.minecraft.net")
        TEXTURES(MojangServices.SERVICE_MINECRAFT_TEXTURES),
        @SerializedName("mojang.net")
        MOJANG(MojangServices.SERVICE_MOJANG);

        @Getter private final URL url;

        Service(URL url) {
            this.url = url;
        }

    }

    public enum State {

        @SerializedName("green")
        NO_ISSUES,
        @SerializedName("yellow")
        SOME_ISSUES,
        @SerializedName("red")
        SERVICE_UNAVAILABLE

    }

}