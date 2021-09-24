package gg.sbs.api.database.models.skills;

import gg.sbs.api.database.SqlRepository;

import java.util.List;

public class SkillRepository extends SqlRepository<SkillModel> {
    public List<SkillModel> findAll() {
        return findAllImpl(SkillModel.class);
    }
}
