package gg.sbs.api.data.sql.models.accessories;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {
    public List<AccessoryModel> findAll() {
        return findAllImpl(AccessoryModel.class);
    }
}
