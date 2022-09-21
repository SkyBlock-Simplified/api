package dev.sbs.api.data.model.skyblock.dungeon_data.dungeons;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonSqlRepository extends SqlRepository<DungeonSqlModel> {

    public DungeonSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
