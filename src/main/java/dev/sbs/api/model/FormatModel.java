package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.awt.*;

public interface FormatModel extends Model {

    String getKey();

    char getCode();

    Color getRgb();

    boolean isFormat();

}
