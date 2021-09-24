package gg.sbs.api.data.sql.models.skills;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class SkillRepository extends SqlRepository<SkillModel> {
    public List<SkillModel> findAll() {
        return findAllImpl(SkillModel.class);
    }
}
