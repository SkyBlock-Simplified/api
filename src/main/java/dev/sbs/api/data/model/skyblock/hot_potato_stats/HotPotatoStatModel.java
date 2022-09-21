package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

import java.util.List;

public interface HotPotatoStatModel extends Model {

    String getGroupKey();

    List<String> getItemTypes();

    StatModel getStat();

    Integer getValue();

}
