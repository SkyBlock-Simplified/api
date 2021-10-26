package dev.sbs.api.model.sql.reforges;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {

    public ReforgeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ReforgeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
