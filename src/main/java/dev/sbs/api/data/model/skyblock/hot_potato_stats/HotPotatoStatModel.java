package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

public interface HotPotatoStatModel extends Model {

    ReforgeTypeModel getType();

    StatModel getStat();

    Integer getValue();

}
