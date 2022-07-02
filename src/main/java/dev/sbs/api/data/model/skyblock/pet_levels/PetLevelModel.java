package dev.sbs.api.data.model.skyblock.pet_levels;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface PetLevelModel extends Model {

    RarityModel getRarity();

    default Integer getRarityOrdinal() {
        return this.getRarity().getOrdinal();
    }

    Integer getLevel();

    Double getValue();

}
