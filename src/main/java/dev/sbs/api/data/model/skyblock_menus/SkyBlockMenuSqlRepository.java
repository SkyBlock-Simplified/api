package dev.sbs.api.data.model.skyblock_menus;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockMenuSqlRepository extends SqlRepository<SkyBlockMenuSqlModel> {

    public SkyBlockMenuSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkyBlockMenuSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
