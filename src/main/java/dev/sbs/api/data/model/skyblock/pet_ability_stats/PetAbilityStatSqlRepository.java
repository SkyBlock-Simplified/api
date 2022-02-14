package dev.sbs.api.data.model.skyblock.pet_ability_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetAbilityStatSqlRepository extends SqlRepository<PetAbilityStatSqlModel> {

    public PetAbilityStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
