package gg.sbs.api.database.repositories.itemtypes;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class ItemTypeRepository extends SqlRepository<ItemTypeModel> {
    public List<ItemTypeModel> findAll() {
        return findAllImpl(ItemTypeModel.class);
    }
}
