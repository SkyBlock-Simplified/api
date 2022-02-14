package dev.sbs.api.data.model.skyblock.dungeon_floor_sizes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class DungeonFloorSizeSqlRepository extends SqlRepository<DungeonFloorSizeSqlModel> {

    public DungeonFloorSizeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
