package dev.sbs.api.data.model.dungeon_floors;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonFloorSqlRepository extends SqlRepository<DungeonFloorSqlModel> {

    public DungeonFloorSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
