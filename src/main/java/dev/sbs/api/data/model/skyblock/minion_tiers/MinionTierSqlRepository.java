package dev.sbs.api.data.model.skyblock.minion_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MinionTierSqlRepository extends SqlRepository<MinionTierSqlModel> {

    public MinionTierSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
