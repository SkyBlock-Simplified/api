package gg.sbs.api.data.sql.models.accessoryfamilies;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {
    public List<AccessoryFamilyModel> findAll() {
        return findAllImpl(AccessoryFamilyModel.class);
    }

    @Override
    public <S> AccessoryFamilyModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(AccessoryFamilyModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> AccessoryFamilyModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(AccessoryFamilyModel.class, predicates);
    }
}