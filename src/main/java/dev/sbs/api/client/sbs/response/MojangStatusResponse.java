package dev.sbs.api.client.sbs.response;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.HttpStatus;
import dev.sbs.api.client.sbs.MojangServices;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;

import java.net.URL;

public class MojangStatusResponse {

    @Getter
    private ConcurrentMap<Service, State> allStates;

    public State getState(Service service) {
        return this.allStates.get(service);
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

        @Getter
        private final URL url;

        Service(URL url) {
            this.url = url;
        }

    }

    public static class State {

        @Getter
        private boolean success;
        private int code;

        public HttpStatus getHttpStatus() {
            return HttpStatus.getByCode(this.code);
        }

    }

}
