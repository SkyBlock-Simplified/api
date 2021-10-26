package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.List;

public interface PetAbilityStatModel extends Model {

    PetAbilityModel getAbility();

    int getPriority();

    List<Integer> getRarities();

    double getBaseValue();

    String getExpression();

}
