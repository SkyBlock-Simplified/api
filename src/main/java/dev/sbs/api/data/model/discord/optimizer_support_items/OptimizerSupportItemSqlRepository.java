package dev.sbs.api.data.model.discord.optimizer_support_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class OptimizerSupportItemSqlRepository extends SqlRepository<OptimizerSupportItemSqlModel> {

    public OptimizerSupportItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}