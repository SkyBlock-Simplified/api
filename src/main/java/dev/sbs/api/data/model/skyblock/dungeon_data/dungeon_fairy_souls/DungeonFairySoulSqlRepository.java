package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_fairy_souls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonFairySoulSqlRepository extends SqlRepository<DungeonFairySoulSqlModel> {

    public DungeonFairySoulSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
