package dev.sbs.api.data.model.skyblock.dungeons;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class DungeonSqlRepository extends SqlRepository<DungeonSqlModel> {

    public DungeonSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
