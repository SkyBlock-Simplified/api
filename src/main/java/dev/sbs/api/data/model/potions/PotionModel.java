package dev.sbs.api.data.model.potions;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.formats.FormatModel;

public interface PotionModel extends Model {

    String getKey();

    String getName();

    FormatModel getFormat();

    String getDescription();

    boolean isBuff();

}
