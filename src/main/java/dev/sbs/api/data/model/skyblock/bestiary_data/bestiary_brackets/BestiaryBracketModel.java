package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_brackets;

import dev.sbs.api.data.model.Model;

public interface BestiaryBracketModel extends Model {

    Integer getBracket();

    Integer getTier();

    Integer getTotalKillsRequired();

}
