package dev.sbs.api.model.sql.slayers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SlayerRepository extends SqlRepository<SlayerSqlModel> {

    public SlayerRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SlayerRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
