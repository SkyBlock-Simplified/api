package gg.sbs.api.database.repositories.reforges;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {
    public List<ReforgeModel> findAll() {
        return findAllImpl(ReforgeModel.class);
    }
}
