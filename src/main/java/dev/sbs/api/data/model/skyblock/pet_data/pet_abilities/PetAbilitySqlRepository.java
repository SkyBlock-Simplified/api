package dev.sbs.api.data.model.skyblock.pet_data.pet_abilities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetAbilitySqlRepository extends SqlRepository<PetAbilitySqlModel> {

    public PetAbilitySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
