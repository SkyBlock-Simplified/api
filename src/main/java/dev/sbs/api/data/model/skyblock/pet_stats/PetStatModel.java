package dev.sbs.api.data.model.skyblock.pet_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.pets.PetModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

import java.util.List;

public interface PetStatModel extends Model {

    PetModel getPet();

    StatModel getStat();

    int getOrdinal();

    List<Integer> getRarities();

    double getBaseValue();

    double getLevelBonus();

}
