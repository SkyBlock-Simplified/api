package dev.sbs.api.model.sql.fairysouls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class FairySoulRepository extends SqlRepository<FairySoulSqlModel> {

    public FairySoulRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public FairySoulRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
