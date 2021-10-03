package gg.sbs.api.data.sql.models.collections;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class CollectionRepository extends SqlRepository<CollectionModel> {
    public List<CollectionModel> findAll() {
        return findAllImpl(CollectionModel.class);
    }
}
