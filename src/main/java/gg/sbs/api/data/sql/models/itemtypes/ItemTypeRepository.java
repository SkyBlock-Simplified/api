package gg.sbs.api.data.sql.models.itemtypes;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class ItemTypeRepository extends SqlRepository<ItemTypeModel> {
    public List<ItemTypeModel> findAll() {
        return findAllImpl(ItemTypeModel.class);
    }
}
