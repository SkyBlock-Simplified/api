package dev.sbs.api.data.model.skyblock_bag_sizes;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock_bags.SkyBlockBagModel;

public interface SkyBlockBagSizeModel extends Model {

    SkyBlockBagModel getBag();

    Integer getCollectionTier();

    Integer getSlotCount();

}
