package dev.sbs.api.data.model.pet_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.pets.PetModel;
import dev.sbs.api.data.model.stats.StatModel;

import java.util.List;

public interface PetStatModel extends Model {

    PetModel getPet();

    StatModel getStat();

    int getOrdinal();

    List<Integer> getRarities();

    double getBaseValue();

    double getLevelBonus();

}
