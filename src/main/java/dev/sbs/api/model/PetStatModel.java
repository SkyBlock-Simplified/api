package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.List;

public interface PetStatModel extends Model {

    PetModel getPet();

    StatModel getStat();

    List<Integer> getRarities();

    double getBaseValue();

    String getExpression();

}
