package gg.sbs.api.data.sql.models.itemtypes;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class ItemTypeRepository extends SqlRepository<ItemTypeModel> {
    public List<ItemTypeModel> findAll() {
        return findAllImpl(ItemTypeModel.class);
    }

    @Override
    public <S> ItemTypeModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(ItemTypeModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> ItemTypeModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(ItemTypeModel.class, predicates);
    }
}
