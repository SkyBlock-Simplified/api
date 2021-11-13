package dev.sbs.api.data.model.dungeon_floor_sizes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonFloorSizeSqlRepository extends SqlRepository<DungeonFloorSizeSqlModel> {

    public DungeonFloorSizeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
