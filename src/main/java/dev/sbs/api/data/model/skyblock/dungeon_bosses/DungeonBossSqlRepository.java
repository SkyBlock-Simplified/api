package dev.sbs.api.data.model.skyblock.dungeon_bosses;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonBossSqlRepository extends SqlRepository<DungeonBossSqlModel> {

    public DungeonBossSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}