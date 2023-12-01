package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_brackets.BestiaryBracketModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_categories.BestiaryCategoryModel;

public interface BestiaryFamilyModel extends Model {

    String getKey();

    String getName();

    BestiaryBracketModel getBracket();

    BestiaryCategoryModel getCategory();

    Integer getOrdinal();

}
