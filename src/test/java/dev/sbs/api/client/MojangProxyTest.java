package dev.sbs.api.client;


import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.mojang.MojangProxy;
import dev.sbs.api.client.mojang.request.MojangApiRequest;
import dev.sbs.api.client.mojang.response.MojangMultiUsernameResponse;
import dev.sbs.api.client.mojang.response.MojangProfile;
import dev.sbs.api.client.mojang.response.MojangUsernameResponse;
import org.junit.jupiter.api.Test;

public class MojangProxyTest {

    @Test
    public void makeMojangRequests_ok() {
        MojangProxy mojangProxy = SimplifiedApi.getMojangProxy();
        MojangApiRequest apiRequest = mojangProxy.getApiRequest();
        MojangUsernameResponse usernameResponse = apiRequest.getUniqueId("CraftedFury");
        System.out.println(usernameResponse.getUsername() + " : " + usernameResponse.getUniqueId());
    }

    @Test
    public void makeMultiMojangRequests_ok() {
        MojangProxy mojangProxy = SimplifiedApi.getMojangProxy();
        MojangApiRequest apiRequest = mojangProxy.getApiRequest();
        MojangMultiUsernameResponse multiUsernameResponse = apiRequest.getMultipleUniqueIds("CraftedFury", "GoldenDusk");
        multiUsernameResponse.getProfiles().forEach(usernameResponse -> System.out.println(usernameResponse.getUsername() + " : " + usernameResponse.getUniqueId()));
    }

    @Test
    public void makeMojangProfile_ok() {
        MojangProxy mojangProxy = SimplifiedApi.getMojangProxy();
        MojangProfile mojangProfile = mojangProxy.getMojangProfile("CraftedFury");
        System.out.println(mojangProfile.getUsername() + " : " + mojangProfile.getUniqueId());
    }

}
