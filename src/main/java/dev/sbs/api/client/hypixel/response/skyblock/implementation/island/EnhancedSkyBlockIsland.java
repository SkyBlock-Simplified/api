package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account.CommunityUpgrades;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account.EnhancedCommunityUpgrades;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.member.EnhancedMember;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.member.Member;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionModel;
import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Getter
public class EnhancedSkyBlockIsland extends SkyBlockIsland {

    private final @NotNull ConcurrentLinkedMap<UUID, EnhancedMember> enhancedMembers;
    private final @NotNull Optional<ProfileModel> profileTypeModel;
    private final @NotNull Optional<EnhancedCommunityUpgrades> communityUpgrades;

    public EnhancedSkyBlockIsland(@NotNull SkyBlockIsland skyBlockIsland) {
        super(
            skyBlockIsland.getIslandId(),
            skyBlockIsland.getCommunityUpgrades(),
            skyBlockIsland.getBanking(),
            skyBlockIsland.getGameMode(),
            skyBlockIsland.getProfileName(),
            skyBlockIsland.isSelected(),
            skyBlockIsland.getMembers()
        );

        this.enhancedMembers = this.getMembers()
            .pairStream()
            .mapValue(Member::asEnhanced)
            .collect(Concurrent.toLinkedMap());
        this.profileTypeModel = this.getProfileName()
            .flatMap(profileName -> SimplifiedApi.getRepositoryOf(ProfileModel.class)
                .findCached()
                .findFirst(ProfileModel::getKey, profileName)
            );
        this.communityUpgrades = super.getCommunityUpgrades().map(CommunityUpgrades::asEnhanced);
    }

    public @NotNull Collection getCollection(@NotNull CollectionModel type) {
        ConcurrentLinkedMap<CollectionItemModel, Long> collectedItems = Concurrent.newLinkedMap();
        ConcurrentLinkedMap<CollectionItemModel, Integer> collectionUnlocked = Concurrent.newLinkedMap();

        this.getEnhancedMembers().forEach((uniqueId, member) -> {
            Collection collection = member.getCollection(type);
            collectedItems.forEach(entry -> collection.getCollected().put(entry.getKey(), Math.max(collection.getCollected().getOrDefault(entry.getKey(), 0L), entry.getValue())));
            collectionUnlocked.putAll(collection.getUnlocked());
        });

        return new Collection(
            type,
            collectedItems,
            collectionUnlocked
        );
    }

    public @NotNull ConcurrentList<Integer> getCraftedMinions(@NotNull MinionModel type) {
        return this.getEnhancedMembers()
            .pairStream()
            .values()
            .flatMap(member -> member.getCraftedMinions(type).stream())
            .distinct()
            .collect(Concurrent.toUnmodifiableList())
            .sorted(Comparator.naturalOrder());
    }

    public int getUniqueMinions() {
        return this.getMembers()
            .pairStream()
            .values()
            .mapToInt(member -> member.getPlayerData().getCraftedMinions().size())
            .sum() + this.getCommunityUpgrades()
            .map(communityUpgrades -> communityUpgrades.getUpgraded()
                .stream()
                .filter(upgraded -> upgraded.getUpgrade() == CommunityUpgrades.Type.MINION_SLOTS)
                .count()
            )
            .map(Long::intValue)
            .orElse(0);
    }

}