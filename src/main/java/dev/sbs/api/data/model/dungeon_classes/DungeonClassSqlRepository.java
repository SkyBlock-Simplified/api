package dev.sbs.api.data.model.dungeon_classes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonClassSqlRepository extends SqlRepository<DungeonClassSqlModel> {

    public DungeonClassSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public DungeonClassSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
