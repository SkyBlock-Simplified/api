package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class HotPotatoStatSqlRepository extends SqlRepository<HotPotatoStatSqlModel> {

    public HotPotatoStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
