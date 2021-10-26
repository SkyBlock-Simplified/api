package dev.sbs.api.model.sql.npcs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.model.sql.fairysouls.FairySoulSqlModel;
import lombok.NonNull;

public class NpcRepository extends SqlRepository<NpcSqlModel> {

    public NpcRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public NpcRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
