package dev.sbs.api.model.sql.items.itemtypes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemCategoryRepository extends SqlRepository<ItemCategorySqlModel> {

    public ItemCategoryRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ItemCategoryRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
