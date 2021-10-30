package dev.sbs.api.data.model.fairy_souls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class FairySoulSqlRepository extends SqlRepository<FairySoulSqlModel> {

    public FairySoulSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public FairySoulSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
