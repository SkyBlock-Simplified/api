package gg.sbs.api.data.sql.models.potions;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class PotionRepository extends SqlRepository<PotionModel> {
    public List<PotionModel> findAll() {
        return findAllImpl(PotionModel.class);
    }

    @Override
    public <S> PotionModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(PotionModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> PotionModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(PotionModel.class, predicates);
    }
}