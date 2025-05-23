package dev.sbs.api.client;


import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.impl.mojang.MojangProxy;
import dev.sbs.api.client.impl.mojang.exception.MojangApiException;
import dev.sbs.api.client.impl.mojang.request.MojangApiRequest;
import dev.sbs.api.client.impl.mojang.response.MojangMultiUsernameResponse;
import dev.sbs.api.client.impl.mojang.response.MojangUsernameResponse;
import dev.sbs.api.client.impl.sbs.request.SbsRequest;
import dev.sbs.api.scheduler.Scheduler;
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
        //MojangProfileResponse mojangProfileResponse = mojangProxy.getMojangProfile("CraftedFury");
        SbsRequest sbsRequest = SimplifiedApi.getApiRequest(SbsRequest.class);

        for (int i = 0; i < 1_000; i++) {
            int x = i;
            try {
                SimplifiedApi.getScheduler().scheduleAsync(() -> {
                    MojangUsernameResponse mojangUsernameResponse = sbsRequest.getTestProfileFromUsername("CraftedFury");
                    System.out.println("Finished request #" + x);
                });
            } catch (MojangApiException mojangApiException) {
                mojangApiException.printStackTrace();
            }
        }

        while (SimplifiedApi.getScheduler().getTasks().notEmpty())
            Scheduler.sleep(1);

        //System.out.println(mojangProfileResponse.getUsername() + " : " + mojangProfileResponse.getUniqueId());
        //String json = SimplifiedApi.getGson().toJson(mojangProfileResponse);
        //System.out.println(json);
    }

}
