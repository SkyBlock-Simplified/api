package dev.sbs.api.data.model.skyblock.bag_sizes;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.bags.BagModel;

public interface BagSizeModel extends Model {

    BagModel getBag();

    int getCollectionTier();

    int getSlotCount();

}
