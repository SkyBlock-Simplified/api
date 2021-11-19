package dev.sbs.api.data.model.skyblock.potion_group_items;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.potion_groups.PotionGroupModel;
import dev.sbs.api.data.model.skyblock.potion_tiers.PotionTierModel;

public interface PotionGroupItemModel extends Model {

    PotionGroupModel getPotionGroup();

    PotionTierModel getPotionTier();

}
