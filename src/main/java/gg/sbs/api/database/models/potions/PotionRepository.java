package gg.sbs.api.database.models.potions;

import gg.sbs.api.database.SqlRepository;

import java.util.List;

public class PotionRepository extends SqlRepository<PotionModel> {
    public List<PotionModel> findAll() {
        return findAllImpl(PotionModel.class);
    }
}