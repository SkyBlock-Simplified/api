package dev.sbs.api.data.model.skyblock.bonus_data.bonus_reforge_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforges.ReforgeModel;

public interface BonusReforgeStatModel extends BuffEffectsModel<Object, Double> {

    ReforgeModel getReforge();

}
