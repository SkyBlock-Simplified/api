package dev.sbs.api.model.sql.fairysouls.dungeonfairysouls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonFairySoulRepository extends SqlRepository<DungeonFairySoulSqlModel> {

    public DungeonFairySoulRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public DungeonFairySoulRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
