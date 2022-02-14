package dev.sbs.api.data.model.skyblock.dungeon_floors;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonFloorSqlRepository extends SqlRepository<DungeonFloorSqlModel> {

    public DungeonFloorSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
