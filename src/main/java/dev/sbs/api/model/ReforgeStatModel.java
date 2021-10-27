package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.Map;

public interface ReforgeStatModel extends Model {

    String getKey();

    RarityModel getRarity();

    Map<String, Object> getEffects();

}
