package gg.sbs.api.data.sql.models.collectionitems;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class CollectionItemRepository extends SqlRepository<CollectionItemModel> {
    public List<CollectionItemModel> findAll() {
        return findAllImpl(CollectionItemModel.class);
    }

    @Override
    public <S> CollectionItemModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(CollectionItemModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> CollectionItemModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(CollectionItemModel.class, predicates);
    }
}
