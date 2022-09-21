package dev.sbs.api.data.model.skyblock.reforge_data.reforge_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class ReforgeStatSqlRepository extends SqlRepository<ReforgeStatSqlModel> {

    public ReforgeStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
