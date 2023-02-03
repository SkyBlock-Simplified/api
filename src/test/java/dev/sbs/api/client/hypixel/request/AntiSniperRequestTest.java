package dev.sbs.api.client.hypixel.request;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.antisniper.request.NickRequest;
import dev.sbs.api.client.antisniper.response.DeNickResponse;
import dev.sbs.api.client.antisniper.response.FindNickResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class AntiSniperRequestTest {

    private static final NickRequest ANTISNIPER_DENICK_REQUEST;

    static {
        ANTISNIPER_DENICK_REQUEST = SimplifiedApi.getWebApi(NickRequest.class);
    }

    @Test
    public void getNickname_ok() {
        FindNickResponse findNickResponse = ANTISNIPER_DENICK_REQUEST.getNicknameFromUser("GoldenDusk");

        if (findNickResponse.isSuccess()) {
            assert findNickResponse.getPlayer().isPresent();
            FindNickResponse.Player player = findNickResponse.getPlayer().get();
            MatcherAssert.assertThat(player.getNick(), Matchers.equalTo("MegaCoolGirly"));
        }
    }

    @Test
    public void getUser_ok() {
        DeNickResponse deNickResponse = ANTISNIPER_DENICK_REQUEST.getUserFromNickname("TheLostFreak");

        if (deNickResponse.isSuccess()) {
            assert deNickResponse.getPlayer().isPresent();
            DeNickResponse.Player player = deNickResponse.getPlayer().get();
            MatcherAssert.assertThat(player.getName(), Matchers.equalTo("CraftedFury"));
        }
    }

}
