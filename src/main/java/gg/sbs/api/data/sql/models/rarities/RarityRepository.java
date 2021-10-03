package gg.sbs.api.data.sql.models.rarities;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class RarityRepository extends SqlRepository<RarityModel> {
    public List<RarityModel> findAll() {
        return findAllImpl(RarityModel.class);
    }

    @Override
    public <S> RarityModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(RarityModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> RarityModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(RarityModel.class, predicates);
    }
}