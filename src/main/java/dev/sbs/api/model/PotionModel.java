package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.Map;

public interface PotionModel extends Model {

    String getName();

    int getItemLevel();

    Map<String, Object> getEffects();

}
