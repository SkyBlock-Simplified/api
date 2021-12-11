package dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BonusPetAbilityStatSqlRepository extends SqlRepository<BonusPetAbilityStatSqlModel> {

    public BonusPetAbilityStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
