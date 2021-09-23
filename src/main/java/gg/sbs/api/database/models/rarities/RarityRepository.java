package gg.sbs.api.database.models.rarities;

import gg.sbs.api.database.models.SqlRepository;

import java.util.List;

public class RarityRepository extends SqlRepository<RarityModel> {
    public List<RarityModel> findAll() {
        return findAllImpl(RarityModel.class);
    }
}