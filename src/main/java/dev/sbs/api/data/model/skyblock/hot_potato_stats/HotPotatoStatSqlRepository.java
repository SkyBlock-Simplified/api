package dev.sbs.api.data.model.skyblock.hot_potato_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class HotPotatoStatSqlRepository extends SqlRepository<HotPotatoStatSqlModel> {

    public HotPotatoStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
