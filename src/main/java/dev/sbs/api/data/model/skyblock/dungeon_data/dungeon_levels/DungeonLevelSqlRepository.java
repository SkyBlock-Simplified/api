package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonLevelSqlRepository extends SqlRepository<DungeonLevelSqlModel> {

    public DungeonLevelSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
