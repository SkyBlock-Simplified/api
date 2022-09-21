package dev.sbs.api.data.model.skyblock.bonus_data.bonus_pet_ability_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BonusPetAbilityStatSqlRepository extends SqlRepository<BonusPetAbilityStatSqlModel> {

    public BonusPetAbilityStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
