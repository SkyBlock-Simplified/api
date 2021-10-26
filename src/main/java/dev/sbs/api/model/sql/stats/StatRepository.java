package dev.sbs.api.model.sql.stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class StatRepository extends SqlRepository<StatModel> {

    public StatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public StatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
