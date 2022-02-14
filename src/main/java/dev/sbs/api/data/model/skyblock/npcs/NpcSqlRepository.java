package dev.sbs.api.data.model.skyblock.npcs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class NpcSqlRepository extends SqlRepository<NpcSqlModel> {

    public NpcSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
