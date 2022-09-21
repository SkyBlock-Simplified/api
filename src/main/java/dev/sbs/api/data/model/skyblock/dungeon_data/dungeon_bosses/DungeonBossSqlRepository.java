package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_bosses;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonBossSqlRepository extends SqlRepository<DungeonBossSqlModel> {

    public DungeonBossSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
