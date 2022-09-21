package dev.sbs.api.data.model.skyblock.minion_data.minion_tier_upgrades;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MinionTierUpgradeSqlRepository extends SqlRepository<MinionTierUpgradeSqlModel> {

    public MinionTierUpgradeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
