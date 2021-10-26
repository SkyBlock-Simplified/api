package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface StatModel extends Model {

    String getKey();

    String getName();

    char getSymbol();

    FormatModel getFormat();

    int getOrdinal();

}
