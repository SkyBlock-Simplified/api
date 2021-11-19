package dev.sbs.api.data.model.skyblock.minion_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierSqlRepository extends SqlRepository<MinionTierSqlModel> {

    public MinionTierSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
