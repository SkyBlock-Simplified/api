package gg.sbs.api.data.sql.models.items;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class ItemRepository extends SqlRepository<ItemModel> {
    public List<ItemModel> findAll() {
        return findAllImpl(ItemModel.class);
    }
}