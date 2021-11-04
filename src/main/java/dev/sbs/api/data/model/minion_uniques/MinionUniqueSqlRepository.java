package dev.sbs.api.data.model.minion_uniques;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionUniqueSqlRepository extends SqlRepository<MinionUniqueSqlModel> {

    public MinionUniqueSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionUniqueSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
