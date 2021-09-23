package gg.sbs.api.database.models.accessories;

import gg.sbs.api.database.models.SqlRepository;

import java.util.List;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {
    public List<AccessoryModel> findAll() {
        return findAllImpl(AccessoryModel.class);
    }
}
