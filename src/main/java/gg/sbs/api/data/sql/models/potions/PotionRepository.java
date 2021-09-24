package gg.sbs.api.data.sql.models.potions;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class PotionRepository extends SqlRepository<PotionModel> {
    public List<PotionModel> findAll() {
        return findAllImpl(PotionModel.class);
    }
}