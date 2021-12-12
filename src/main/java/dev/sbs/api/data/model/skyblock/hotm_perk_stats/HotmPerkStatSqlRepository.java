package dev.sbs.api.data.model.skyblock.hotm_perk_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class HotmPerkStatSqlRepository extends SqlRepository<HotmPerkStatSqlModel> {

    public HotmPerkStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
