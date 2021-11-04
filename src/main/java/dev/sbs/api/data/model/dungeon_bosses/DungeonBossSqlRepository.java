package dev.sbs.api.data.model.dungeon_bosses;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonBossSqlRepository extends SqlRepository<DungeonBossSqlModel> {

    public DungeonBossSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public DungeonBossSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
