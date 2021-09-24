package gg.sbs.api.data.sql.models.rarities;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class RarityRepository extends SqlRepository<RarityModel> {
    public List<RarityModel> findAll() {
        return findAllImpl(RarityModel.class);
    }
}