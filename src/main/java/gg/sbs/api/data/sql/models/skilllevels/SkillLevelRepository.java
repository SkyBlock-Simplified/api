package gg.sbs.api.data.sql.models.skilllevels;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class SkillLevelRepository extends SqlRepository<SkillLevelModel> {
    public List<SkillLevelModel> findAll() {
        return findAllImpl(SkillLevelModel.class);
    }
}
