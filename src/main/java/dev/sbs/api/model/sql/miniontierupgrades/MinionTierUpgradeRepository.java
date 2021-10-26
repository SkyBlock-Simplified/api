package dev.sbs.api.model.sql.miniontierupgrades;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierUpgradeRepository extends SqlRepository<MinionTierUpgradeModel> {

    public MinionTierUpgradeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionTierUpgradeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
