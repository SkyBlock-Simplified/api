package dev.sbs.api.data.model.skyblock.pet_data.pets;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetSqlRepository extends SqlRepository<PetSqlModel> {

    public PetSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
