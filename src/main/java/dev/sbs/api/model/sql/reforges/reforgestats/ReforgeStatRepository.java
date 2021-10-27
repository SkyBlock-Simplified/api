package dev.sbs.api.model.sql.reforges.reforgestats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeStatRepository extends SqlRepository<ReforgeStatSqlModel> {

    public ReforgeStatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ReforgeStatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
