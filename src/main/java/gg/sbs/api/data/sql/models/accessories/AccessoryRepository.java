package gg.sbs.api.data.sql.models.accessories;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class AccessoryRepository extends SqlRepository<AccessoryModel> {
    public List<AccessoryModel> findAll() {
        return findAllImpl(AccessoryModel.class);
    }

    @Override
    public <S> AccessoryModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(AccessoryModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> AccessoryModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(AccessoryModel.class, predicates);
    }
}
