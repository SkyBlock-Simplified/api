package dev.sbs.api.data.model.skyblock.bonus_reforge_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeModel;

public interface BonusReforgeStatModel extends BuffEffectsModel<Double> {

    ReforgeModel getReforge();

}
