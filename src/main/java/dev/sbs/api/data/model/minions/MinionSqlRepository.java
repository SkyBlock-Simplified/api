package dev.sbs.api.data.model.minions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionSqlRepository extends SqlRepository<MinionSqlModel> {

    public MinionSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
