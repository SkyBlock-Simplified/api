package gg.sbs.api.apiclients.hypixel.response.skyblock;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.apiclients.exception.HypixelApiException;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.model.items.ItemModel;
import gg.sbs.api.data.sql.model.items.ItemRepository;
import gg.sbs.api.util.ListUtil;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.ConcurrentList;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class SkyBlockIslandTest {

    private static final HypixelSkyBlockData hypixelSkyBlockData;

    static {
        SimplifiedApi.enableDatabase();
        hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);
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
            Optional<SkyBlockIsland.PetInfo> optionalWolfPet = pets.stream().filter(petInfo -> petInfo.getName().equals("WOLF")).findFirst();
            Optional<SkyBlockIsland.PetInfo> optionalDragonPet = pets.stream().filter(petInfo -> petInfo.getName().equals("ENDER_DRAGON")).findFirst();

            optionalWolfPet.ifPresent(wolfInfo -> {
                optionalDragonPet.ifPresent(dragInfo -> {
                    int wolf_hs = wolfInfo.getPet().hashCode();
                    int drag_hs = dragInfo.getPet().hashCode();

                    MatcherAssert.assertThat(wolf_hs, Matchers.not(drag_hs));

                    wolfInfo.getHeldItem().ifPresent(itemModel -> {
                        MatcherAssert.assertThat(itemModel.getRarity().getOrdinal(), Matchers.greaterThanOrEqualTo(0));
                    });
                });
            });

            MatcherAssert.assertThat(member.getUniqueId(), Matchers.equalTo(uniqueId));
        } catch (HypixelApiException exception) {
            MatcherAssert.assertThat(exception.getHttpStatus().getCode(), Matchers.greaterThan(400));
        }
    }

}