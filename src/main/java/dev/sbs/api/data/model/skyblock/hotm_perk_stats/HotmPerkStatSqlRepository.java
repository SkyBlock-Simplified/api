package dev.sbs.api.data.model.skyblock.hotm_perk_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class HotmPerkStatSqlRepository extends SqlRepository<HotmPerkStatSqlModel> {

    public HotmPerkStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
