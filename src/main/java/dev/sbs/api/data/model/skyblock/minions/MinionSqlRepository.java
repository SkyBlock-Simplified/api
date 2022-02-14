package dev.sbs.api.data.model.skyblock.minions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MinionSqlRepository extends SqlRepository<MinionSqlModel> {

    public MinionSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
