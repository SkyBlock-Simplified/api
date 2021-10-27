package dev.sbs.api.model.sql.reforges.reforgecategories;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeCategoryRepository extends SqlRepository<ReforgeCategorySqlModel> {

    public ReforgeCategoryRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ReforgeCategoryRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
