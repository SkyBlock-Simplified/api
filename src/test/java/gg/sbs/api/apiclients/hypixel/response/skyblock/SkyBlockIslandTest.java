package gg.sbs.api.apiclients.hypixel.response.skyblock;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.apiclients.exception.HypixelApiException;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.ConcurrentList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class SkyBlockIslandTest {

    private static final HypixelSkyBlockData hypixelSkyBlockData;
    private static final HypixelApiBuilder hypixelApi;

    static {
        hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);
        hypixelApi = SimplifiedApi.getServiceManager().getProvider(HypixelApiBuilder.class);
    }

    @Test
    public void getIsland_ok() {
        try {
            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(uniqueId);
            SkyBlockIsland island = profiles.getIslands().get(0);
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(0);

            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            SkyBlockIsland.Member member = optionalMember.get();
            //ConcurrentList<SkyBlockIsland.JacobsFarming.Contest> contests = member.getJacobsFarming().getContests();
            ConcurrentList<SkyBlockIsland.PetInfo> pets = member.getPets();
            MatcherAssert.assertThat(member.getUniqueId(), Matchers.equalTo(uniqueId));
        } catch (HypixelApiException exception) {
            String error = "error";
        }
    }

}