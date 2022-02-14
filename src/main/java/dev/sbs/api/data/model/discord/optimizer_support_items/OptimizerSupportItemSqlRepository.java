package dev.sbs.api.data.model.discord.optimizer_support_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class OptimizerSupportItemSqlRepository extends SqlRepository<OptimizerSupportItemSqlModel> {

    public OptimizerSupportItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
