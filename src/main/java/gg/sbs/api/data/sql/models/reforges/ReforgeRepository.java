package gg.sbs.api.data.sql.models.reforges;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {
    public List<ReforgeModel> findAll() {
        return findAllImpl(ReforgeModel.class);
    }
}
