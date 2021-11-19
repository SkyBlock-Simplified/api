package dev.sbs.api.data.model.skyblock.pet_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetTypeSqlRepository extends SqlRepository<PetTypeSqlModel> {

    public PetTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
