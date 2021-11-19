package dev.sbs.api.data.model.skyblock.minion_tier_upgrades;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierUpgradeSqlRepository extends SqlRepository<MinionTierUpgradeSqlModel> {

    public MinionTierUpgradeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
