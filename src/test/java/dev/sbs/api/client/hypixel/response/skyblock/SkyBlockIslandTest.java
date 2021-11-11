package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.data.model.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.dungeons.DungeonModel;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.minion_tier_upgrades.MinionTierUpgradeModel;
import dev.sbs.api.data.model.minion_tier_upgrades.MinionTierUpgradeSqlModel;
import dev.sbs.api.data.model.minion_tier_upgrades.MinionTierUpgradeSqlRepository;
import dev.sbs.api.data.model.minion_tiers.MinionTierModel;
import dev.sbs.api.data.model.minions.MinionModel;
import dev.sbs.api.data.model.skills.SkillModel;
import dev.sbs.api.data.model.skills.SkillSqlRepository;
import dev.sbs.api.data.model.slayers.SlayerModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.StringUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public class SkyBlockIslandTest {

    @Test
    public void getIsland_ok() {
        try {
            long start = System.currentTimeMillis();
            System.out.println("Database Starting... ");
            SimplifiedApi.enableDatabase();
            long end = System.currentTimeMillis();
            System.out.println("Database Started in " + (end - start) + "ms");
            HypixelSkyBlockData hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);

            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(uniqueId);
            SkyBlockIsland island = profiles.getIslands().get(0);
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(0);

            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            SkyBlockIsland.Member member = optionalMember.get();
            //member.getCollection(SimplifiedApi.getSqlRepository(SkillSqlRepository.class).findFirstOrNullCached(SkillModel::getKey, "FARMING"));
            double skillAverage = member.getSkillAverage();
            ConcurrentMap<SkillModel, SkyBlockIsland.Member.Weight> skillWeights = member.getSkillWeight();
            ConcurrentMap<SlayerModel, SkyBlockIsland.Member.Weight> slayerWeights = member.getSlayerWeight();
            ConcurrentMap<DungeonModel, SkyBlockIsland.Member.Weight> dungeonWeights = member.getDungeonWeight();
            ConcurrentMap<DungeonClassModel, SkyBlockIsland.Member.Weight> dungeonClassWeights = member.getDungeonClassWeight();
            ConcurrentList<SkyBlockIsland.JacobsFarming.Contest> contests = member.getJacobsFarming().getContests();

            try {
                Session session = SimplifiedApi.getSqlSession().openSession();
                MinionTierUpgradeSqlModel testMTU = SimplifiedApi.getSqlRepository(MinionTierUpgradeSqlRepository.class).findFirstOrNullCached(
                        FilterFunction.combine(MinionTierUpgradeModel::getMinionTier, FilterFunction.combine(MinionTierModel::getItem, ItemModel::getItemId)), "WHEAT_GENERATOR_1");
                MinionTierModel mtm = testMTU.getMinionTier();
                ItemModel ic = testMTU.getItemCost();

                String testing = ""; // This waits until the 4th last model to load, for testing purposes
            } catch (SqlException ignore) { }

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
        //} catch (SqlException sqlException) {
            //Assertions.fail();
        }
    }

}
