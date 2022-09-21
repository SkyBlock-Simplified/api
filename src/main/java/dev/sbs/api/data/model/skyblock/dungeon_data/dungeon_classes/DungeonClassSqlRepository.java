package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonClassSqlRepository extends SqlRepository<DungeonClassSqlModel> {

    public DungeonClassSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
