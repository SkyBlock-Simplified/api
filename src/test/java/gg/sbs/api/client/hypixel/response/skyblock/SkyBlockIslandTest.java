package gg.sbs.api.client.hypixel.response.skyblock;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.client.exception.HypixelApiException;
import gg.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.util.helper.StringUtil;
import gg.sbs.api.util.concurrent.ConcurrentList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class SkyBlockIslandTest {

    @Test
    public void getIsland_ok() {
        try {
            long start = System.currentTimeMillis();
            SimplifiedApi.enableDatabase();
            long end = System.currentTimeMillis();
            System.out.println("Database Startup: " + (end - start) + "ms");
            HypixelSkyBlockData hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);

            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(uniqueId);
            SkyBlockIsland island = profiles.getIslands().get(0);
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(0);

            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            SkyBlockIsland.Member member = optionalMember.get();
            ConcurrentList<SkyBlockIsland.JacobsFarming.Contest> contests = member.getJacobsFarming().getContests();
            ConcurrentList<SkyBlockIsland.PetInfo> pets = member.getPets();
            Optional<SkyBlockIsland.PetInfo> optionalWolfPet = pets.stream().filter(petInfo -> petInfo.getName().equals("WOLF")).findFirst();
            Optional<SkyBlockIsland.PetInfo> optionalDragonPet = pets.stream().filter(petInfo -> petInfo.getName().equals("ENDER_DRAGON")).findFirst();

            optionalWolfPet.ifPresent(wolfInfo -> {
                System.out.println("Looking for doOnLoad error x6"); // TODO: Generates massive error after this on 6, 7, or 8
                wolfInfo.getHeldItem().ifPresent(itemModel -> {
                    System.out.println("Looking for doOnLoad error x7");
                    MatcherAssert.assertThat(itemModel.getRarity().getOrdinal(), Matchers.greaterThanOrEqualTo(0));
                });

                optionalDragonPet.ifPresent(dragInfo -> {
                    System.out.println("Looking for doOnLoad error x8");
                    wolfInfo.getPet().ifPresent(wolfPet -> {
                        System.out.println("Looking for doOnLoad error x9");
                        dragInfo.getPet().ifPresent(dragPet -> {
                            MatcherAssert.assertThat(wolfPet, Matchers.notNullValue());
                            MatcherAssert.assertThat(dragPet, Matchers.notNullValue());

                            int wolf_hs = wolfPet.hashCode();
                            int drag_hs = dragPet.hashCode();

                            MatcherAssert.assertThat(wolf_hs, Matchers.not(drag_hs));
                        });
                    });
                });
            });

            MatcherAssert.assertThat(member.getUniqueId(), Matchers.equalTo(uniqueId));
        } catch (HypixelApiException exception) {
            MatcherAssert.assertThat(exception.getHttpStatus().getCode(), Matchers.greaterThan(400));
        }
    }

}