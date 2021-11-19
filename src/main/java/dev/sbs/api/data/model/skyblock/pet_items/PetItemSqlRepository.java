package dev.sbs.api.data.model.skyblock.pet_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetItemSqlRepository extends SqlRepository<PetItemSqlModel> {

    public PetItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
