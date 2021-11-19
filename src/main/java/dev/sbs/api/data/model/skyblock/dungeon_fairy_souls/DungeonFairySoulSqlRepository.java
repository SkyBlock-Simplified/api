package dev.sbs.api.data.model.skyblock.dungeon_fairy_souls;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonFairySoulSqlRepository extends SqlRepository<DungeonFairySoulSqlModel> {

    public DungeonFairySoulSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
