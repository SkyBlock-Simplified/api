package gg.sbs.api.data.sql.models.skills;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class SkillRepository extends SqlRepository<SkillModel> {
    public List<SkillModel> findAll() {
        return findAllImpl(SkillModel.class);
    }

    @Override
    public <S> SkillModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(SkillModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> SkillModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(SkillModel.class, predicates);
    }
}
