package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.TestConfig;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.island.playerstats.PlayerStats;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_tier_upgrades.MinionTierUpgradeModel;
import dev.sbs.api.data.model.skyblock.minion_tiers.MinionTierModel;
import dev.sbs.api.data.model.skyblock.pets.PetModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.helper.StringUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class SkyBlockIslandTest {

    private static final TestConfig testConfig;

    static {
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            testConfig = new TestConfig(currentDir.getParentFile(), "testsql");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }
    }

    @Test
    public void getPlayerStats_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.connectDatabase(testConfig);
            System.out.println("Database initialized in " + SimplifiedApi.getSqlSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSqlSession().getStartupTime() + "ms");
            HypixelSkyBlockData hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);

            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(uniqueId);
            SkyBlockIsland island = profiles.getLastPlayed(uniqueId); // Bingo Profile = 0
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(uniqueId);

            // Did Hypixel Reply / Does a Member Exist
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            SkyBlockIsland.Member member = optionalMember.get();
            PlayerStats playerStats = island.getPlayerStats(member);

            //playerStats.getStats(PlayerStats.Type.ACTIVE_PET)
            //    .forEach((statModel, statData) -> System.out.println("PET: " + statModel.getKey() + ": " + statData.getTotal() + " (" + statData.getBase() + " / " + statData.getBonus() + ")"));

            playerStats.getCombinedStats().forEach((statModel, statData) -> System.out.println(statModel.getKey() + ": " + statData.getTotal() + " (" + statData.getBase() + " / " + statData.getBonus() + ")"));
        } catch (HypixelApiException hypixelApiException) {
            hypixelApiException.printStackTrace();
            MatcherAssert.assertThat(hypixelApiException.getHttpStatus().getCode(), Matchers.greaterThan(400));
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.fail();
        } finally {
            SimplifiedApi.disconnectDatabase();
        }
    }

    @Test
    public void getIsland_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.connectDatabase(testConfig);
            System.out.println("Database initialized in " + SimplifiedApi.getSqlSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSqlSession().getStartupTime() + "ms");
            HypixelSkyBlockData hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);

            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(uniqueId);
            SkyBlockIsland island = profiles.getLastPlayed(uniqueId); // Bingo Profile = 0
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(uniqueId);

            // Did Hypixel Reply / Does a Member Exist
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            // skills, skill_levels, slayers, slayer_levels, dungeons, dungeon_classes, dungeon_levels
            SkyBlockIsland.Member member = optionalMember.get();
            double skillAverage = member.getSkillAverage();
            ConcurrentMap<SkyBlockIsland.Skill, SkyBlockIsland.Experience.Weight> skillWeights = member.getSkillWeight();
            ConcurrentMap<SkyBlockIsland.Slayer, SkyBlockIsland.Experience.Weight> slayerWeights = member.getSlayerWeight();
            ConcurrentMap<SkyBlockIsland.Dungeon, SkyBlockIsland.Experience.Weight> dungeonWeights = member.getDungeonWeight();
            ConcurrentMap<SkyBlockIsland.Dungeon.Class, SkyBlockIsland.Experience.Weight> dungeonClassWeights = member.getDungeonClassWeight();
            assert member.getJacobsFarming().isPresent();
            ConcurrentList<SkyBlockIsland.JacobsFarming.Contest> contests = member.getJacobsFarming().get().getContests();

            // skills, skill_levels
            Repository<SkillModel> skillRepo = SimplifiedApi.getRepositoryOf(SkillModel.class);
            SkillModel combatSkillModel = skillRepo.findFirstOrNull(SkillModel::getKey, "COMBAT");
            ConcurrentList<SkillLevelModel> skillLevels = SimplifiedApi.getRepositoryOf(SkillLevelModel.class).findAll(SkillLevelModel::getSkill, combatSkillModel);
            MatcherAssert.assertThat(skillLevels.size(), Matchers.equalTo(60));

            // collection_items, collections
            SkyBlockIsland.Collection sbCollection = member.getCollection(combatSkillModel);
            SackModel sbSack = SimplifiedApi.getRepositoryOf(SackModel.class).findFirstOrNull(SackModel::getKey, "MINING");
            MatcherAssert.assertThat(member.getSack(sbSack).getStored().size(), Matchers.greaterThan(0));

            // minion_tier_upgrades, minion_tiers, items
            MinionTierUpgradeModel wheatGen11 = SimplifiedApi.getRepositoryOf(MinionTierUpgradeModel.class).findFirstOrNull(
                SearchFunction.combine(MinionTierUpgradeModel::getMinionTier, SearchFunction.combine(MinionTierModel::getItem, ItemModel::getItemId)), "WHEAT_GENERATOR_11");

            MatcherAssert.assertThat(wheatGen11.getMinionTier().getMinion().getKey(), Matchers.equalTo("WHEAT"));
            MatcherAssert.assertThat(wheatGen11.getItemCost().getItemId(), Matchers.equalTo("ENCHANTED_HAY_BLOCK"));

            // rarities, pets, pet_items, pet_exp_scales
            ConcurrentList<SkyBlockIsland.PetInfo> pets = member.getPets();
            Optional<SkyBlockIsland.PetInfo> optionalSpiderPet = pets.stream().filter(petInfo -> petInfo.getName().equals("SPIDER")).findFirst();
            Optional<SkyBlockIsland.PetInfo> optionalDragonPet = pets.stream().filter(petInfo -> petInfo.getName().equals("ENDER_DRAGON")).findFirst();

            Optional<SkyBlockIsland.PetInfo> optionalTestPet = pets.stream().filter(petInfo -> petInfo.getName().equals("BEE")).findFirst();
            optionalTestPet.ifPresent(testPetInfo -> {
                ConcurrentList<Double> tiers = testPetInfo.getExperienceTiers();
                double tierSum = tiers.stream().mapToDouble(value -> value).sum();
                double testExperience = testPetInfo.getExperience();
                double testX1 = testPetInfo.getProgressExperience();
                double testX2 = testPetInfo.getNextExperience();
                double testX3 = testPetInfo.getMissingExperience();
                double testX4 = testPetInfo.getTotalProgressPercentage();
                int testLevel = testPetInfo.getLevel();
                int testRawLevel = testPetInfo.getRawLevel();
                int testMaxLevel = testPetInfo.getMaxLevel();
                double testPercentage = testPetInfo.getProgressPercentage();
                String stop = "here";
            });

            optionalSpiderPet.ifPresent(spiderPetInfo -> optionalDragonPet.ifPresent(dragInfo -> {
                spiderPetInfo.getHeldItem().ifPresent(itemModel -> MatcherAssert.assertThat(itemModel.getRarity().getOrdinal(), Matchers.greaterThanOrEqualTo(0)));

                double spiderExp = spiderPetInfo.getExperience();
                MatcherAssert.assertThat(spiderExp, Matchers.greaterThan(0.0));
                int spiderLevel = spiderPetInfo.getLevel();
                MatcherAssert.assertThat(spiderLevel, Matchers.equalTo(100));

                PetModel spiderPet = spiderPetInfo.getPet().get();
                PetModel dragPet = dragInfo.getPet().get();

                RarityModel commonRarity = SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getKey, "COMMON");
                MatcherAssert.assertThat(spiderPet.getLowestRarity(), Matchers.equalTo(commonRarity));

                int wolf_hs = spiderPet.hashCode();
                int drag_hs = dragPet.hashCode();

                MatcherAssert.assertThat(wolf_hs, Matchers.not(drag_hs));
            }));

            MatcherAssert.assertThat(member.getUniqueId(), Matchers.equalTo(uniqueId));
        } catch (HypixelApiException hypixelApiException) {
            hypixelApiException.printStackTrace();
            MatcherAssert.assertThat(hypixelApiException.getHttpStatus().getCode(), Matchers.greaterThan(400));
        } catch (Exception exception) {
            exception.printStackTrace();
            Assertions.fail();
        } finally {
            SimplifiedApi.getSqlSession().shutdown();
        }
    }

}
