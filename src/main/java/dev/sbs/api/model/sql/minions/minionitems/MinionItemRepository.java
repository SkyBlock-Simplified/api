package dev.sbs.api.model.sql.minions.minionitems;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionItemRepository extends SqlRepository<MinionItemSqlModel> {

    public MinionItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
