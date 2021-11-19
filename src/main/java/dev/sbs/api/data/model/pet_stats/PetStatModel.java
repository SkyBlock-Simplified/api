package dev.sbs.api.data.model.pet_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.pets.PetModel;
import dev.sbs.api.data.model.stats.StatModel;

import java.util.List;

public interface PetStatModel extends Model {

    PetModel getPet();

    StatModel getStat();

    Integer getOrdinal();

    List<Integer> getRarities();

    Double getBaseValue();

    Double getLevelBonus();

}
