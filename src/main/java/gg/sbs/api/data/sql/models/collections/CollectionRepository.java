package gg.sbs.api.data.sql.models.collections;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class CollectionRepository extends SqlRepository<CollectionModel> {
    public List<CollectionModel> findAll() {
        return findAllImpl(CollectionModel.class);
    }

    @Override
    public <S> CollectionModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(CollectionModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> CollectionModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(CollectionModel.class, predicates);
    }
}
