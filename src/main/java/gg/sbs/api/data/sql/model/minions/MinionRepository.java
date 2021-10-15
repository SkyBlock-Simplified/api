package gg.sbs.api.data.sql.model.minions;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionRepository extends SqlRepository<MinionModel> {

    public MinionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}