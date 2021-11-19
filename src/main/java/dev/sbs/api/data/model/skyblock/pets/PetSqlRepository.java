package dev.sbs.api.data.model.skyblock.pets;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetSqlRepository extends SqlRepository<PetSqlModel> {

    public PetSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
