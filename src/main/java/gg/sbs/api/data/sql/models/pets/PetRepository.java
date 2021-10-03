package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class PetRepository extends SqlRepository<PetModel> {
    public List<PetModel> findAll() {
        return findAllImpl(PetModel.class);
    }

    @Override
    public <S> PetModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(PetModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> PetModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(PetModel.class, predicates);
    }
}
