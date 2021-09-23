package gg.sbs.api.database.repositories.potions;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class PotionRepository extends SqlRepository<PotionModel> {
    public List<PotionModel> findAll() {
        return findAllImpl(PotionModel.class);
    }
}