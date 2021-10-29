package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.List;

public interface PetAbilityStatModel extends Model {

    PetAbilityModel getAbility();

    List<Integer> getRarities();

    StatModel getStat();

    double getBaseValue();

    String getExpression();

}
