package dev.sbs.api.model.sql.minions.miniontierupgrades;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierUpgradeRepository extends SqlRepository<MinionTierUpgradeSqlModel> {

    public MinionTierUpgradeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionTierUpgradeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
