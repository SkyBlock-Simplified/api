package gg.sbs.api.data.sql.models.skilllevels;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class SkillLevelRepository extends SqlRepository<SkillLevelModel> {
    public List<SkillLevelModel> findAll() {
        return findAllImpl(SkillLevelModel.class);
    }

    @Override
    public <S> SkillLevelModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(SkillLevelModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> SkillLevelModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(SkillLevelModel.class, predicates);
    }
}
