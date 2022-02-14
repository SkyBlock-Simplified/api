package dev.sbs.api.data.model.skyblock.pet_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetTypeSqlRepository extends SqlRepository<PetTypeSqlModel> {

    public PetTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
