package gg.sbs.api.database.models.itemtypes;

import gg.sbs.api.database.models.SqlRepository;

import java.util.List;

public class ItemTypeRepository extends SqlRepository<ItemTypeModel> {
    public List<ItemTypeModel> findAll() {
        return findAllImpl(ItemTypeModel.class);
    }
}
