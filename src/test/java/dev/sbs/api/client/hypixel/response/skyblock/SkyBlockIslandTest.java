package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.exception.HypixelApiException;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.skyblock.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.minion_tier_upgrades.MinionTierUpgradeModel;
import dev.sbs.api.data.model.skyblock.minion_tiers.MinionTierModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerModel;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.tuple.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
            SkyBlockIsland island = profiles.getIslands().get(1); // TODO: Bingo Profile = 0
            Optional<SkyBlockIsland.Member> optionalMember = island.getMember(0);

            // Did Hypixel Reply / Does a Member Exist
            MatcherAssert.assertThat(profiles.isSuccess(), Matchers.equalTo(true));
            MatcherAssert.assertThat(optionalMember.isPresent(), Matchers.equalTo(true));

            // skills, skill_levels, slayers, slayer_levels, dungeons, dungeon_classes, dungeon_levels
            SkyBlockIsland.Member member = optionalMember.get();
            double skillAverage = member.getSkillAverage();
            ConcurrentMap<SkillModel, SkyBlockIsland.Member.Weight> skillWeights = member.getSkillWeight();
            ConcurrentMap<SlayerModel, SkyBlockIsland.Member.Weight> slayerWeights = member.getSlayerWeight();
            ConcurrentMap<DungeonModel, SkyBlockIsland.Member.Weight> dungeonWeights = member.getDungeonWeight();
            ConcurrentMap<DungeonClassModel, SkyBlockIsland.Member.Weight> dungeonClassWeights = member.getDungeonClassWeight();
            ConcurrentList<SkyBlockIsland.JacobsFarming.Contest> contests = member.getJacobsFarming().getContests();

            SkyBlockIsland.PlayerStats playerStats = member.getPlayerStats(); // TODO: Work in Progress
            ConcurrentMap<String, Double> playerStatMap = playerStats.getStats()
                .stream()
                .map(entry -> Pair.of(entry.getKey().getKey(), entry.getValue().getBase() + entry.getValue().getBonus()))
                .collect(Concurrent.toMap());

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
                    FilterFunction.combine(MinionTierUpgradeModel::getMinionTier, FilterFunction.combine(MinionTierModel::getItem, ItemModel::getItemId)), "WHEAT_GENERATOR_11");

            MatcherAssert.assertThat(wheatGen11.getMinionTier().getMinion().getKey(), Matchers.equalTo("WHEAT"));
            MatcherAssert.assertThat(wheatGen11.getItemCost().getItemId(), Matchers.equalTo("ENCHANTED_HAY_BLOCK"));

            // rarities, pets, pet_items, pet_exp_scales
            ConcurrentList<SkyBlockIsland.PetInfo> pets = member.getPets();
            Optional<SkyBlockIsland.PetInfo> optionalSpiderPet = pets.stream().filter(petInfo -> petInfo.getName().equals("SPIDER")).findFirst();
            Optional<SkyBlockIsland.PetInfo> optionalDragonPet = pets.stream().filter(petInfo -> petInfo.getName().equals("ENDER_DRAGON")).findFirst();

            optionalSpiderPet.ifPresent(spiderPetInfo -> optionalDragonPet.ifPresent(dragInfo -> {
                spiderPetInfo.getHeldItem().ifPresent(itemModel -> MatcherAssert.assertThat(itemModel.getRarity().getOrdinal(), Matchers.greaterThanOrEqualTo(0)));

                double spiderExp = spiderPetInfo.getExperience();
                MatcherAssert.assertThat(spiderExp, Matchers.greaterThan(0.0));
                int spiderLevel = spiderPetInfo.getLevel();
                MatcherAssert.assertThat(spiderLevel, Matchers.equalTo(100));

                spiderPetInfo.getPet().ifPresent(wolfPet -> {
                    RarityModel commonRarity = SimplifiedApi.getRepositoryOf(RarityModel.class).findFirstOrNull(RarityModel::getKey, "COMMON");
                    MatcherAssert.assertThat(wolfPet.getLowestRarity(), Matchers.equalTo(commonRarity));

                    dragInfo.getPet().ifPresent(dragPet -> {
                        int wolf_hs = wolfPet.hashCode();
                        int drag_hs = dragPet.hashCode();

                        MatcherAssert.assertThat(wolf_hs, Matchers.not(drag_hs));
                    });
                });
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
