package gg.sbs.api.data.sql.model.reforges;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeRepository extends SqlRepository<ReforgeModel> {

    public ReforgeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ReforgeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}