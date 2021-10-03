package gg.sbs.api.data.sql.models.items;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class ItemRepository extends SqlRepository<ItemModel> {
    public List<ItemModel> findAll() {
        return findAllImpl(ItemModel.class);
    }

    @Override
    public <S> ItemModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(ItemModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> ItemModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(ItemModel.class, predicates);
    }
}