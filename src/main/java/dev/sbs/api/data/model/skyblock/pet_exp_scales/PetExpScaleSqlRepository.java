package dev.sbs.api.data.model.skyblock.pet_exp_scales;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetExpScaleSqlRepository extends SqlRepository<PetExpScaleSqlModel> {

    public PetExpScaleSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
