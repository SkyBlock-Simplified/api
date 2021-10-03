package gg.sbs.api.data.sql.models.collectionitemtiers;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class CollectionItemTierRepository extends SqlRepository<CollectionItemTierModel> {
    public List<CollectionItemTierModel> findAll() {
        return findAllImpl(CollectionItemTierModel.class);
    }

    @Override
    public <S> CollectionItemTierModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(CollectionItemTierModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> CollectionItemTierModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(CollectionItemTierModel.class, predicates);
    }
}
