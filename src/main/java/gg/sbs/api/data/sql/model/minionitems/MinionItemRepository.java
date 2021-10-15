package gg.sbs.api.data.sql.model.minionitems;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionItemRepository extends SqlRepository<MinionItemModel> {

    public MinionItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}