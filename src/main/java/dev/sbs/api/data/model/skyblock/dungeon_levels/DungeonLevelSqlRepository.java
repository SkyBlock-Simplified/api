package dev.sbs.api.data.model.skyblock.dungeon_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonLevelSqlRepository extends SqlRepository<DungeonLevelSqlModel> {

    public DungeonLevelSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
