package dev.sbs.api.data.model.skyblock.pet_abilities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetAbilitySqlRepository extends SqlRepository<PetAbilitySqlModel> {

    public PetAbilitySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
