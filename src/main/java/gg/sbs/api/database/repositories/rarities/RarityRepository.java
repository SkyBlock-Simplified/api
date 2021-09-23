package gg.sbs.api.database.repositories.rarities;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class RarityRepository extends SqlRepository<RarityModel> {
    public List<RarityModel> findAll() {
        return findAllImpl(RarityModel.class);
    }
}