package gg.sbs.api.data.sql.models.collectionitems;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class CollectionItemRepository extends SqlRepository<CollectionItemModel> {
    public List<CollectionItemModel> findAll() {
        return findAllImpl(CollectionItemModel.class);
    }
}
