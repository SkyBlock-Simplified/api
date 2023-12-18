package dev.sbs.api.client;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.request.HypixelRequest;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockProfilesResponse;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Collection;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.EnhancedSkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.JacobsContest;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.Slayer;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.bestiary.Bestiary;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.dungeon.Dungeon;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.member.EnhancedMember;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.member.Member;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet.EnhancedPet;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet.Pet;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.ProfileStats;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.profile_stats.data.ObjectData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.skill.Skill;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.weight.Weight;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tier_upgrades.MinionTierUpgradeModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tiers.MinionTierModel;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.search.SearchFunction;
import dev.sbs.api.util.data.tuple.pair.Pair;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.stream.StreamUtil;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SkyBlockIslandTest {

    @Test
    public void getGuildLevels_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
            System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartupTime() + "ms");
            HypixelRequest hypixelRequest = SimplifiedApi.getApiRequest(HypixelRequest.class);
            String guildName = "SkyBlock Simplified";

            hypixelRequest.getGuildByName(guildName)
                .getGuild()
                .ifPresent(guild -> {
                    ConcurrentMap<Pair<UUID, String>, Integer> playerLevels = guild.getMembers()
                        .stream()
                        .map(member -> hypixelRequest.getProfiles(member.getUniqueId())
                            .getIslands()
                            .stream()
                            .max(Comparator.comparingInt(island -> island.getMembers()
                                .get(member.getUniqueId())
                                .getLeveling()
                                .getExperience()
                            ))
                            .map(island -> Pair.of(
                                Pair.of(
                                    member.getUniqueId(),
                                    hypixelRequest.getPlayer(member.getUniqueId()).getPlayer().getDisplayName()
                                ),
                                island.getMembers()
                                    .get(member.getUniqueId())
                                    .getLeveling()
                                    .getLevel()
                            ))
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
            SimplifiedApi.getSessionManager().disconnect();
        }
    }

    @Test
    public void getPlayerStats_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
            System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartupTime() + "ms");
            HypixelRequest hypixelRequest = SimplifiedApi.getApiRequest(HypixelRequest.class);

            UUID uniqueId = StringUtil.toUUID("f33f51a7-9691-4076-abda-f66e3d047a71"); // CraftedFury
            //UUID uniqueId = StringUtil.toUUID("df5e1701-809c-48be-9b0d-ef50b83b009e"); // GoldenDusk
            SkyBlockProfilesResponse profiles = hypixelRequest.getProfiles(uniqueId);

            // Did Hypixel Reply
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            EnhancedSkyBlockIsland island = profiles.getSelected().asEnhanced(); // Bingo Profile = 0

            // Does Member Exist
            MatcherAssert.assertThat(island.getMembers().containsKey(uniqueId), Matchers.equalTo(true));
            EnhancedMember member = island.getEnhancedMembers().get(uniqueId);

            // SkyBlock Levels
            int exp1 = member.getLeveling().getExperience();
            int explevel = member.getLeveling().getLevel();

            Bestiary bestiaryData = member.getBestiary();

            ProfileStats profileStats = island.getProfileStats(member);
            profileStats.getStats(ProfileStats.Type.ACCESSORY_POWER)
                .stream()
                .forEach(entry -> System.out.println(
                    entry.getKey().getKey() + ": " +
                        entry.getValue().getTotal() + " (" + entry.getValue().getBase() + " / " + entry.getValue().getBonus() + ")")
                );

            System.out.println("All Accessories: " + profileStats.getAccessoryBag().getAccessories().size());
            System.out.println("Filtered Accessories: " + profileStats.getAccessoryBag().getFilteredAccessories().size());
            System.out.println("Magic Power: " + profileStats.getAccessoryBag().getMagicalPower());
            System.out.println("\n---\n");

            profileStats.getAccessoryBag()
                .getFilteredAccessories()
                .sorted(ObjectData::getRarity)
                .stream()
                .collect(Collectors.groupingBy(accessory -> accessory.getRarity().getKey(), Collectors.counting()))
                .forEach((rarity, count) -> System.out.println(rarity + ": " + count));

            System.out.println("\n---\n");

            profileStats.getAccessoryBag()
                .getFilteredAccessories()
                .sorted(ObjectData::getRarity)
                .stream()
                .map(accessory -> accessory.getAccessory().getItem().getItemId() + ": " + accessory.getRarity().getName() + " (" + accessory.getRarity().getMagicPowerMultiplier() + ")")
                .forEach(System.out::println);

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
            SimplifiedApi.getSessionManager().disconnect();
        }
    }

    @Test
    public void getIsland_ok() {
        try {
            System.out.println("Database Starting... ");
            SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
            System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitializationTime() + "ms");
            System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartupTime() + "ms");
            HypixelRequest hypixelRequest = SimplifiedApi.getApiRequest(HypixelRequest.class);
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

            SkyBlockProfilesResponse profiles = hypixelRequest.getProfiles(checkThis.getKey());
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));

            //SkyBlockIsland island = profiles.getSelected(); // Bingo Profile = 0
            Optional<SkyBlockIsland> pineappleIsland = profiles.getIsland(checkThis.getRight().getKey());
            assert pineappleIsland.isPresent();
            SkyBlockIsland island = pineappleIsland.get();
            Member member = island.getMembers().get(checkThis.getKey());
            EnhancedMember enhancedMember = member.asEnhanced();

            int petScore = member.getPetData().getPetScore();

            /*int uniques = island.getMinions()
                .stream()
                .mapToInt(minion -> minion.getUnlocked().size())
                .sum();*/

            // skills, skill_levels, slayers, slayer_levels, dungeons, dungeon_classes, dungeon_levels
            double skillAverage = enhancedMember.getSkillAverage();
            ConcurrentMap<Skill.Type, Weight> skillWeights = enhancedMember.getSkillWeight();
            ConcurrentMap<Slayer.Type, Weight> slayerWeights = enhancedMember.getSlayerWeight();
            ConcurrentMap<Dungeon.Type, Weight> dungeonWeights = enhancedMember.getDungeonWeight();
            ConcurrentMap<Dungeon.Class.Type, Weight> dungeonClassWeights = enhancedMember.getDungeonClassWeight();
            ConcurrentList<JacobsContest.Contest> contests = enhancedMember.getJacobsContest().getContests();

            // skills, skill_levels
            Repository<SkillModel> skillRepo = SimplifiedApi.getRepositoryOf(SkillModel.class);
            SkillModel combatSkillModel = skillRepo.findFirstOrNull(SkillModel::getKey, "COMBAT");
            ConcurrentList<SkillLevelModel> skillLevels = SimplifiedApi.getRepositoryOf(SkillLevelModel.class).findAll(SkillLevelModel::getSkill, combatSkillModel);
            MatcherAssert.assertThat(skillLevels.size(), Matchers.equalTo(60));

            // collection_items, collections
            Repository<CollectionModel> collectionRepo = SimplifiedApi.getRepositoryOf(CollectionModel.class);
            CollectionModel collectionModel = collectionRepo.findFirstOrNull(CollectionModel::getKey, combatSkillModel.getKey());
            Collection sbCollection = member.asEnhanced().getCollection(collectionModel);
            SackModel sbSack = SimplifiedApi.getRepositoryOf(SackModel.class).findFirstOrNull(SackModel::getKey, "MINING");
            //MatcherAssert.assertThat(member.getSack(sbSack).getStored().size(), Matchers.greaterThan(0));

            // minion_tier_upgrades, minion_tiers, items
            MinionTierUpgradeModel wheatGen11 = SimplifiedApi.getRepositoryOf(MinionTierUpgradeModel.class).findFirstOrNull(
                SearchFunction.combine(MinionTierUpgradeModel::getMinionTier, SearchFunction.combine(MinionTierModel::getItem, ItemModel::getItemId)), "WHEAT_GENERATOR_11");

            MatcherAssert.assertThat(wheatGen11.getMinionTier().getMinion().getKey(), Matchers.equalTo("WHEAT"));
            MatcherAssert.assertThat(wheatGen11.getItemCost().getItemId(), Matchers.equalTo("ENCHANTED_HAY_BLOCK"));

            // rarities, pets, pet_items, pet_exp_scales
            ConcurrentList<Pet> pets = member.getPetData().getPets();
            Optional<Pet> optionalSpiderPet = pets.stream().filter(pet -> pet.getType().equals("SPIDER")).findFirst();
            Optional<Pet> optionalDragonPet = pets.stream().filter(pet -> pet.getType().equals("ENDER_DRAGON")).findFirst();

            Optional<Pet> optionalTestPet = pets.stream().filter(pet -> pet.getType().equals("BEE")).findFirst();
            optionalTestPet.ifPresent(testPet -> {
                EnhancedPet atestPet = testPet.asEnhanced();
                ConcurrentList<Double> tiers = atestPet.getExperienceTiers();
                double tierSum = tiers.stream().mapToDouble(value -> value).sum();
                double testExperience = testPet.getExperience();
                double testX1 = atestPet.getProgressExperience();
                double testX2 = atestPet.getNextExperience();
                double testX3 = atestPet.getMissingExperience();
                double testX4 = atestPet.getTotalProgressPercentage();
                int testLevel = atestPet.getLevel();
                int testRawLevel = atestPet.getRawLevel();
                int testMaxLevel = atestPet.getMaxLevel();
                double testPercentage = atestPet.getProgressPercentage();
                String stop = "here";
            });

            optionalSpiderPet.ifPresent(spiderPetInfo -> optionalDragonPet.ifPresent(dragInfo -> {
                EnhancedPet espiderPetInfo = spiderPetInfo.asEnhanced();
                espiderPetInfo.getHeldItemModel().ifPresent(itemModel -> MatcherAssert.assertThat(itemModel.getRarity().getOrdinal(), Matchers.greaterThanOrEqualTo(0)));

                double spiderExp = spiderPetInfo.getExperience();
                MatcherAssert.assertThat(spiderExp, Matchers.greaterThan(0.0));
                int spiderLevel = espiderPetInfo.getLevel();
                MatcherAssert.assertThat(spiderLevel, Matchers.equalTo(100));

                PetModel spiderPet = espiderPetInfo.getTypeModel().get();
                PetModel dragPet = dragInfo.asEnhanced().getTypeModel().get();

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
            SimplifiedApi.getSessionManager().disconnect();
        }
    }

}
