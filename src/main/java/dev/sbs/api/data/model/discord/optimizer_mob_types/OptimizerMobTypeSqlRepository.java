package dev.sbs.api.data.model.discord.optimizer_mob_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class OptimizerMobTypeSqlRepository extends SqlRepository<OptimizerMobTypeSqlModel> {

    public OptimizerMobTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
