package dev.sbs.api.model.sql.reforges.reforgetypes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeTypeRepository extends SqlRepository<ReforgeTypeSqlModel> {

    public ReforgeTypeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ReforgeTypeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
