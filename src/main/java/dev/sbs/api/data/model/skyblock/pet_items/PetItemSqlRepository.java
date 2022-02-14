package dev.sbs.api.data.model.skyblock.pet_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetItemSqlRepository extends SqlRepository<PetItemSqlModel> {

    public PetItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
