package gg.sbs.api.database.models.reforges;

import gg.sbs.api.database.SqlRepository;

import java.util.List;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {
    public List<ReforgeModel> findAll() {
        return findAllImpl(ReforgeModel.class);
    }
}
