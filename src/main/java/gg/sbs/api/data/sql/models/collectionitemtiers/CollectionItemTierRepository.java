package gg.sbs.api.data.sql.models.collectionitemtiers;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class CollectionItemTierRepository extends SqlRepository<CollectionItemTierModel> {
    public List<CollectionItemTierModel> findAll() {
        return findAllImpl(CollectionItemTierModel.class);
    }
}
