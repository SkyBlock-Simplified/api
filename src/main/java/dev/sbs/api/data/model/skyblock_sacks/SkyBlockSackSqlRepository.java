package dev.sbs.api.data.model.skyblock_sacks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockSackSqlRepository extends SqlRepository<SkyBlockSackSqlModel> {

    public SkyBlockSackSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkyBlockSackSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
