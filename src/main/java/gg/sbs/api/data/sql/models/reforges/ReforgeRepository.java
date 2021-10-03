package gg.sbs.api.data.sql.models.reforges;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {
    public List<ReforgeModel> findAll() {
        return findAllImpl(ReforgeModel.class);
    }

    @Override
    public <S> ReforgeModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(ReforgeModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> ReforgeModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(ReforgeModel.class, predicates);
    }
}
