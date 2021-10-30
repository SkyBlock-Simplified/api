package dev.sbs.api.data.model.formats;

import dev.sbs.api.data.model.Model;

import java.awt.*;

public interface FormatModel extends Model {

    String getKey();

    char getCode();

    Color getRgb();

    boolean isFormat();

}
