package gg.sbs.api.data.sql.model.stats;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class StatRepository extends SqlRepository<StatModel> {

    public StatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public StatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}