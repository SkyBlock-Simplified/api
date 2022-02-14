package dev.sbs.api.data.model.skyblock.reforge_conditions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ReforgeConditionSqlRepository extends SqlRepository<ReforgeConditionSqlModel> {

    public ReforgeConditionSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
