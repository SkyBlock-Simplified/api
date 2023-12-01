package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families.BestiaryFamilyModel;

public interface BestiaryModel extends Model {

    String getKey();

    String getName();

    Integer getLevel();

    BestiaryFamilyModel getFamily();

    Integer getOrdinal();

}
