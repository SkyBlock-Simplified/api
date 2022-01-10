package dev.sbs.api.data.model.skyblock.reforge_conditions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeConditionSqlRepository extends SqlRepository<ReforgeConditionSqlModel> {

    public ReforgeConditionSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
