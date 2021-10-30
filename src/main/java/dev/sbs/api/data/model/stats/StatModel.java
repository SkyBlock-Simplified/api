package dev.sbs.api.data.model.stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.formats.FormatModel;

public interface StatModel extends Model {

    String getKey();

    String getName();

    char getSymbol();

    FormatModel getFormat();

    int getOrdinal();

}
