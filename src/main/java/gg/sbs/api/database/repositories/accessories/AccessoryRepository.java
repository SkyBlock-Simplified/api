package gg.sbs.api.database.repositories.accessories;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {
    public List<AccessoryModel> findAll() {
        return findAllImpl(AccessoryModel.class);
    }
}
