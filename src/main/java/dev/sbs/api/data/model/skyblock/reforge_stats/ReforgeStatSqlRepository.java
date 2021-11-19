package dev.sbs.api.data.model.skyblock.reforge_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ReforgeStatSqlRepository extends SqlRepository<ReforgeStatSqlModel> {

    public ReforgeStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
