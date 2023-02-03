package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.TestConfig;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.HypixelPlayerRequest;
import dev.sbs.api.client.hypixel.request.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Collection;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Dungeon;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Experience;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.JacobsFarming;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.PetInfo;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Skill;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Slayer;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tier_upgrades.MinionTierUpgradeModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tiers.MinionTierModel;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.StreamUtil;
import dev.sbs.api.util.helper.StringUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
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
    public void getGuildLevels_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.connectDatabase(testConfig);
            System.out.println("Database initialized in " + SimplifiedApi.getSqlSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSqlSession().getStartupTime() + "ms");
            HypixelPlayerRequest hypixelPlayerRequest = SimplifiedApi.getWebApi(HypixelPlayerRequest.class);
            HypixelSkyBlockData hypixelSkyBlockData = SimplifiedApi.getWebApi(HypixelSkyBlockData.class);
            String guildName = "SkyBlock Simplified";

            hypixelPlayerRequest.getGuildByName(guildName)
                .getGuild()
                .ifPresent(guild -> {
                    ConcurrentMap<Pair<UUID, String>, Integer> playerLevels = guild.getMembers()
                        .stream()
                        .map(member -> hypixelSkyBlockData.getProfiles(member.getUniqueId())
                            .getIslands()
                            .stream()
                            .max(Comparator.comparingInt(island -> island.getMember(member.getUniqueId())
                                .map(sbmember -> sbmember.getLeveling().getExperience())
                                .orElse(0)
                            ))
                            .map(island -> Pair.of(
                                Pair.of(
                                    member.getUniqueId(),
                                    hypixelPlayerRequest.getPlayer(member.getUniqueId()).getPlayer().getDisplayName()
                                ),
                                island.getMember(member.getUniqueId()).map(sbmember -> sbmember.getLeveling().getLevel()).orElse(0))
                            )
                        )
                        .flatMap(StreamUtil::flattenOptional)
                        .sorted(Comparator.comparingInt(Pair::getRight))
                        .collect(Concurrent.toMap());

                    playerLevels.forEach((pair, level) -> System.out.println(pair.getRight() + ": " + level + " (" + pair.getLeft().toString() + ")"));
                    System.out.println("---");
                    double averageLevel = playerLevels.stream()
                        .filter(entry -> entry.getValue() > 0)
                        .mapToDouble(Map.Entry::getValue)
                        .sum() / playerLevels.size();
                    long belowAverage = playerLevels.stream()
                        .filter(entry -> entry.getValue() > 0)
                        .map(Map.Entry::getValue)
                        .filter(level -> level <= averageLevel)
                        .count();
                    long below200 = playerLevels.stream()
                        .filter(entry -> entry.getValue() > 0)
                        .map(Map.Entry::getValue)
                        .filter(level -> level <= 200)
                        .count();
                    System.out.println("Average Level: " + averageLevel + " / Below Average: " + belowAverage + " / Below 200: " + below200);
                    System.out.println("---");
                    playerLevels.stream()
                        .filter(entry -> entry.getValue() == 0)
                        .forEach(entry -> System.out.println(entry.getKey().getRight() + ": " + entry.getValue() + " (" + entry.getKey().getLeft().toString() + ")"));
                });
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
            SkyBlockIsland island = profiles.getLastPlayed(); // Bingo Profile = 0
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(uniqueId);

            // Did Hypixel Reply / Does a Member Exist
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            SkyBlockIsland.Member member = optionalMember.get();

            // SkyBlock Levels
            int exp1 = member.getLeveling().getExperience();
            int explevel = member.getLeveling().getLevel();


            assert exp1 > 0;
            // Player Stats
            //PlayerStats playerStats = island.getPlayerStats(member);

            //playerStats.getStats(PlayerStats.Type.ACTIVE_PET)
            //    .forEach((statModel, statData) -> System.out.println("PET: " + statModel.getKey() + ": " + statData.getTotal() + " (" + statData.getBase() + " / " + statData.getBonus() + ")"));

            //playerStats.getCombinedStats().forEach((statModel, statData) -> System.out.println(statModel.getKey() + ": " + statData.getTotal() + " (" + statData.getBase() + " / " + statData.getBonus() + ")"));
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
            ProfileModel pineappleProfile = SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirstOrNull(ProfileModel::getKey, "PINEAPPLE");
            ProfileModel bananaProfile = SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirstOrNull(ProfileModel::getKey, "BANANA");

            Pair<UUID, ProfileModel> pair_CraftedFury = Pair.of(
                StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"),
                SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirstOrNull(ProfileModel::getKey, "PINEAPPLE")
            );
            Pair<UUID, ProfileModel> pair_GoldenDusk = Pair.of(
                StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"),
                SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirstOrNull(ProfileModel::getKey, "POMEGRANATE")
            );
            Pair<UUID, ProfileModel> pair_CrazyHjonk = Pair.of(
                StringUtil.toUUID("c360fb57-1e6c-458c-86b5-c971e864536c"),
                SimplifiedApi.getRepositoryOf(ProfileModel.class).findFirstOrNull(ProfileModel::getKey, "BANANA")
            );
            Pair<UUID, ProfileModel> checkThis = pair_CraftedFury;

            SkyBlockProfilesResponse profiles = hypixelSkyBlockData.getProfiles(checkThis.getKey());
            //SkyBlockIsland island = profiles.getLastPlayed(); // Bingo Profile = 0
            Optional<SkyBlockIsland> pineappleIsland = profiles.getIsland(checkThis.getRight());
            assert pineappleIsland.isPresent();
            SkyBlockIsland island = pineappleIsland.get();
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(checkThis.getKey());

            // Did Hypixel Reply / Does a Member Exist
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));
            SkyBlockIsland.Member member = optionalMember.get();

            int petScore = member.getPetScore();

            int uniques = island.getMinions()
                .stream()
                .mapToInt(minion -> minion.getUnlocked().size())
                .sum();

            // skills, skill_levels, slayers, slayer_levels, dungeons, dungeon_classes, dungeon_levels
            double skillAverage = member.getSkillAverage();
            ConcurrentMap<Skill, Experience.Weight> skillWeights = member.getSkillWeight();
            ConcurrentMap<Slayer, Experience.Weight> slayerWeights = member.getSlayerWeight();
            ConcurrentMap<Dungeon, Experience.Weight> dungeonWeights = member.getDungeonWeight();
            ConcurrentMap<Dungeon.Class, Experience.Weight> dungeonClassWeights = member.getDungeonClassWeight();
            ConcurrentList<JacobsFarming.Contest> contests = member.getJacobsFarming().getContests();

            // skills, skill_levels
            Repository<SkillModel> skillRepo = SimplifiedApi.getRepositoryOf(SkillModel.class);
            SkillModel combatSkillModel = skillRepo.findFirstOrNull(SkillModel::getKey, "COMBAT");
            ConcurrentList<SkillLevelModel> skillLevels = SimplifiedApi.getRepositoryOf(SkillLevelModel.class).findAll(SkillLevelModel::getSkill, combatSkillModel);
            MatcherAssert.assertThat(skillLevels.size(), Matchers.equalTo(60));

            // collection_items, collections
            Collection sbCollection = member.getCollection(combatSkillModel);
            SackModel sbSack = SimplifiedApi.getRepositoryOf(SackModel.class).findFirstOrNull(SackModel::getKey, "MINING");
            MatcherAssert.assertThat(member.getSack(sbSack).getStored().size(), Matchers.greaterThan(0));

            // minion_tier_upgrades, minion_tiers, items
            MinionTierUpgradeModel wheatGen11 = SimplifiedApi.getRepositoryOf(MinionTierUpgradeModel.class).findFirstOrNull(
                SearchFunction.combine(MinionTierUpgradeModel::getMinionTier, SearchFunction.combine(MinionTierModel::getItem, ItemModel::getItemId)), "WHEAT_GENERATOR_11");

            MatcherAssert.assertThat(wheatGen11.getMinionTier().getMinion().getKey(), Matchers.equalTo("WHEAT"));
            MatcherAssert.assertThat(wheatGen11.getItemCost().getItemId(), Matchers.equalTo("ENCHANTED_HAY_BLOCK"));

            // rarities, pets, pet_items, pet_exp_scales
            ConcurrentList<PetInfo> pets = member.getPets();
            Optional<PetInfo> optionalSpiderPet = pets.stream().filter(petInfo -> petInfo.getName().equals("SPIDER")).findFirst();
            Optional<PetInfo> optionalDragonPet = pets.stream().filter(petInfo -> petInfo.getName().equals("ENDER_DRAGON")).findFirst();

            Optional<PetInfo> optionalTestPet = pets.stream().filter(petInfo -> petInfo.getName().equals("BEE")).findFirst();
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

            MatcherAssert.assertThat(member.getUniqueId(), Matchers.equalTo(checkThis.getKey()));
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
