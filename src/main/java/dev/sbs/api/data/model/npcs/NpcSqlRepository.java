package dev.sbs.api.data.model.npcs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class NpcSqlRepository extends SqlRepository<NpcSqlModel> {

    public NpcSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public NpcSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
