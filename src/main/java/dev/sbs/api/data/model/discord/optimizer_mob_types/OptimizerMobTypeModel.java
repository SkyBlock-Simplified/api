package dev.sbs.api.data.model.discord.optimizer_mob_types;

import dev.sbs.api.data.model.Model;

public interface OptimizerMobTypeModel extends Model {

    String getKey();

    String getName();

    boolean isActive();

}
