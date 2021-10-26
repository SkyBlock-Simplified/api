package dev.sbs.api.model.sql.items.itemtypes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemTypeRepository extends SqlRepository<ItemTypeSqlModel> {

    public ItemTypeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ItemTypeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
